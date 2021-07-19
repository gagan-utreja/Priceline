package com.priceline.app.view.viewmodel;

import androidx.arch.core.util.Function;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.priceline.app.api.common.Resource;
import com.priceline.app.api.response.detailList.BookDetailsItem;
import com.priceline.app.api.response.detailList.DetailListResponse;
import com.priceline.app.api.response.nameList.NameListResponse;
import com.priceline.app.api.response.nameList.ResultsItem;
import com.priceline.app.model.BookListDetailModel;
import com.priceline.app.model.HeaderItem;
import com.priceline.app.model.SubItem;
import com.priceline.app.repository.Repository;
import com.priceline.app.view.expandable.ExpandableHeaderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class BooksListViewModel extends ViewModel {


    private Repository mRepository;

    // Injection Repository
    @Inject
    public void setRepository(Repository repository) {
        this.mRepository = repository;
    }

    public static BooksListViewModel create(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(BooksListViewModel.class);
    }


    //region Book Name list related methods
    private final MutableLiveData<Boolean> networkInfoObservable = new MutableLiveData<>();
    private LiveData<Resource<ArrayList<AbstractFlexibleItem>>> getAllRetailersResponseLiveData = Transformations.switchMap(networkInfoObservable, new Function<Boolean, LiveData<Resource<ArrayList<AbstractFlexibleItem>>>>() {
        @Override
        public LiveData<Resource<ArrayList<AbstractFlexibleItem>>> apply(final Boolean input) {
            LiveData<Resource<NameListResponse>> resourceLiveData = mRepository.getNameList(input);
            final MediatorLiveData<Resource<ArrayList<AbstractFlexibleItem>>> mediator = new MediatorLiveData<Resource<ArrayList<AbstractFlexibleItem>>>();
            mediator.addSource(resourceLiveData, gitHubDtos -> {
                NameListResponse resp = gitHubDtos.getData();

                Resource<ArrayList<AbstractFlexibleItem>> response = null;
                switch (gitHubDtos.getStatus()) {
                    case LOADING:
                        response = Resource.loading(null);
                        break;
                    case SUCCESS:
                        ArrayList<AbstractFlexibleItem> liveData = transform(resp);
                        response = Resource.success(liveData);
                        break;
                    case ERROR:
                        response = Resource.error(gitHubDtos.getException(), null);
                        break;
                }
                mediator.setValue(response);
            });
            return mediator;
        }
    });

    public void load(boolean refresh) {
        networkInfoObservable.setValue(refresh);
    }

    /**
     * Expose the LiveData So that UI can observe it.
     */
    public LiveData<Resource<ArrayList<AbstractFlexibleItem>>> getNameList() {
        return getAllRetailersResponseLiveData;
    }

    // Transform items into flexible items.
    private ArrayList<AbstractFlexibleItem> transform(NameListResponse nameListResponse) {

        ArrayList<AbstractFlexibleItem> abstractFlexibleItems = new ArrayList<>();

        HeaderItem weeklyHeaderItem = new HeaderItem();
        HeaderItem monthlyHeaderItem = new HeaderItem();
        ArrayList<SubItem> monthlySubItem = new ArrayList<>();
        ArrayList<SubItem> weeklySubItem = new ArrayList<>();
        int subItemCount = 0, weeklyCount = 0, monthlyCount = 0;

        weeklyHeaderItem.setId("1");
        monthlyHeaderItem.setId("2");
        weeklyHeaderItem.setType("WEEKLY");
        monthlyHeaderItem.setType("MONTHLY");
        for (ResultsItem resultsItem : nameListResponse.getResults()) {
            subItemCount++;
            SubItem subItem = new SubItem();
            subItem.setDate(resultsItem.getNewestPublishedDate());
            subItem.setName(resultsItem.getDisplayName());
            subItem.setListNameEncoded(resultsItem.getListNameEncoded());
            subItem.setId("-SB" + subItemCount);
            if (resultsItem.getUpdated().equalsIgnoreCase("MONTHLY")) {
                monthlyCount++;
                monthlySubItem.add(subItem);
            } else {
                weeklyCount++;
                weeklySubItem.add(subItem);
            }

        }

        Collections.sort(weeklySubItem, new Comparator<SubItem>() {
            @Override
            public int compare(SubItem subItem, SubItem t1) {
                return subItem.getDate().compareTo(t1.getDate());
            }
        });

        Collections.sort(monthlySubItem, new Comparator<SubItem>() {
            @Override
            public int compare(SubItem subItem, SubItem t1) {
                return subItem.getDate().compareTo(t1.getDate());
            }
        });
        weeklyHeaderItem.setCount(weeklyCount);
        weeklyHeaderItem.setSubItems(weeklySubItem);
        monthlyHeaderItem.setSubItems(monthlySubItem);
        monthlyHeaderItem.setCount(monthlyCount);
        ExpandableHeaderItem weeklyHeader = new ExpandableHeaderItem(weeklyHeaderItem);
        abstractFlexibleItems.add(weeklyHeader);

        ExpandableHeaderItem monthlyHeader = new ExpandableHeaderItem(monthlyHeaderItem);
        abstractFlexibleItems.add(monthlyHeader);

        return abstractFlexibleItems;

    }

    // endregion

    //region Detail list related methods
    public static class DetailListRequest {

        String listName;
        boolean refresh;

        public DetailListRequest(String listName, Boolean refresh) {
            this.listName = listName;
            this.refresh = refresh;
        }

    }

    private final MutableLiveData<DetailListRequest> mutableLiveData = new MutableLiveData<>();
    private LiveData<Resource<ArrayList<BookListDetailModel>>> resourceLiveData = Transformations.switchMap(mutableLiveData, new Function<DetailListRequest, LiveData<Resource<ArrayList<BookListDetailModel>>>>() {
        @Override
        public LiveData<Resource<ArrayList<BookListDetailModel>>> apply(final DetailListRequest input) {
            LiveData<Resource<DetailListResponse>> resourceLiveData = mRepository.getDetailList(input.refresh, input.listName);
            final MediatorLiveData<Resource<ArrayList<BookListDetailModel>>> mediator = new MediatorLiveData<Resource<ArrayList<BookListDetailModel>>>();
            mediator.addSource(resourceLiveData, gitHubDtos -> {
                DetailListResponse resp = gitHubDtos.getData();

                Resource<ArrayList<BookListDetailModel>> response = null;
                switch (gitHubDtos.getStatus()) {
                    case LOADING:
                        response = Resource.loading(null);
                        break;
                    case SUCCESS:
                        ArrayList<BookListDetailModel> liveData = transformDetailList(resp);
                        response = Resource.success(liveData);
                        break;
                    case ERROR:
                        response = Resource.error(gitHubDtos.getException(), null);
                        break;
                }
                mediator.setValue(response);
            });
            return mediator;
        }
    });

    // Transform stockist order into flexible items.
    private ArrayList<BookListDetailModel> transformDetailList(DetailListResponse detailListResponse) {

        ArrayList<BookListDetailModel> listModelArrayBookListDetail = new ArrayList<>();


        for (com.priceline.app.api.response.detailList.ResultsItem resultsItem : detailListResponse.getResults()) {

            for (BookDetailsItem bookDetailsItem : resultsItem.getBookDetails()) {

                BookListDetailModel bookListDetailModel = new BookListDetailModel();
                bookListDetailModel.setTitle(bookDetailsItem.getTitle());
                bookListDetailModel.setAuthor(bookDetailsItem.getAuthor());
                bookListDetailModel.setDescription(bookDetailsItem.getDescription());
                bookListDetailModel.setPrice(bookDetailsItem.getPrice());
                listModelArrayBookListDetail.add(bookListDetailModel);
            }
        }


        return listModelArrayBookListDetail;

    }


    public void loadDetailList(String listName, boolean refresh) {
        mutableLiveData.setValue(new DetailListRequest(listName, refresh));
    }

    public LiveData<Resource<ArrayList<BookListDetailModel>>> getDetailList() {
        return resourceLiveData;
    }

    //endregion

}