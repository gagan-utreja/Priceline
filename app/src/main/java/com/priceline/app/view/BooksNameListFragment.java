package com.priceline.app.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.priceline.app.api.common.ApiException;
import com.priceline.app.view.viewmodel.BooksListViewModel;
import com.priceline.app.OnItemClickListener;
import com.priceline.app.PricelineApplication;
import com.priceline.app.R;
import com.priceline.app.adapter.ExampleAdapter;
import com.priceline.app.api.common.Resource;
import com.priceline.app.api.response.nameList.ResultsItem;
import com.priceline.app.databinding.BooksNameListFragmentBinding;

import java.util.ArrayList;
import java.util.Collections;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class BooksNameListFragment extends Fragment implements OnItemClickListener {

    private BooksListViewModel booksNameListViewModel;
    private BooksNameListFragmentBinding booksNameListFragmentBinding;
    ExampleAdapter mAdapter;

    //region fragment override methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksNameListViewModel = BooksListViewModel.create(getActivity());
        PricelineApplication.getAppComponent().inject(booksNameListViewModel);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        booksNameListFragmentBinding = BooksNameListFragmentBinding.inflate(inflater, container, false);
        return booksNameListFragmentBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        booksNameListFragmentBinding.recyclerView.setLayoutManager(manager);
        booksNameListFragmentBinding.recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
        setupRecyclerView();
    }

    @Override
    public void onItemClick(String name) {
        BooksDetailsListFragment booksDetailsListFragment = new BooksDetailsListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("list_name", name);
        booksDetailsListFragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.root_layout, booksDetailsListFragment, "DetailList");
        transaction.addToBackStack(null);
        transaction.commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
    }

    //endregion


    // setup recyclerview
    private void setupRecyclerView() {
        FlexibleAdapter.useTag("ExpandableSectionAdapter");
        mAdapter = new ExampleAdapter(Collections.emptyList(), this);
        updateUi(booksNameListViewModel.getNameList());
        // OnItemAdd and OnItemRemove listeners
        mAdapter.addListener(this);
        mAdapter
                .setAutoScrollOnExpand(true)
                .setAnimateToLimit(Integer.MAX_VALUE) //Size limit = MAX_VALUE will always animate the changes
                .setNotifyMoveOfFilteredItems(true) //When true, filtering on big list is very slow!
                .setAnimationOnReverseScrolling(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        booksNameListFragmentBinding.recyclerView.setLayoutManager(mLayoutManager);
        booksNameListFragmentBinding.recyclerView.setAdapter(mAdapter);
        booksNameListFragmentBinding.recyclerView.setHasFixedSize(true); //Size of RV will not change
        booksNameListFragmentBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        booksNameListFragmentBinding.recyclerView.addItemDecoration(new FlexibleItemDecoration(getActivity())
                .withDivider(R.drawable.divider, R.layout.recycler_name_list_item).withDrawOver(true));


        mAdapter.setLongPressDragEnabled(true) //Enable long press to drag items
                .setHandleDragEnabled(true) //Enable handle drag
                //.setDisplayHeadersAtStartUp(true); //Show Headers at startUp: (not necessary if Headers are also Expandable AND expanded at startup)
                .setStickyHeaders(true);

    }

    // udpate the ui using observer
    private void updateUi(LiveData<Resource<ArrayList<AbstractFlexibleItem>>> liveData) {
        // Update the list when the data changes
        liveData.observe(this, new Observer<Resource<ArrayList<AbstractFlexibleItem>>>() {
            @Override
            public void onChanged(Resource<ArrayList<AbstractFlexibleItem>> responseResource) {
                switch (responseResource.getStatus()) {
                    case LOADING: {
                        booksNameListFragmentBinding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    }
                    case ERROR:
                        booksNameListFragmentBinding.progressBar.setVisibility(View.GONE);
                        if (responseResource.getException().getKind() == ApiException.Kind.NETWORK)
                            Toast.makeText(getActivity(), responseResource.getException().getErrorModel().errorMessage, Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), responseResource.getException().getErrorMessage(), Toast.LENGTH_LONG).show();

                        break;
                    case SUCCESS: {
                        booksNameListFragmentBinding.progressBar.setVisibility(View.GONE);
                        mAdapter.updateDataSet(responseResource.getData(), true);
                        break;
                    }
                }

            }
        });
        booksNameListViewModel.load(false);
    }


}