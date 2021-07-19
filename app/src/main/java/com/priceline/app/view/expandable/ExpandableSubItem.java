package com.priceline.app.view.expandable;

import android.animation.Animator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.priceline.app.R;
import com.priceline.app.adapter.ExampleAdapter;
import com.priceline.app.model.SubItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.AnimatorHelper;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * If you don't have many fields in common better to extend directly from
 * {@link eu.davidea.flexibleadapter.items.AbstractFlexibleItem} to benefit of the already
 * implemented methods (getter and setters).
 */
public class ExpandableSubItem extends AbstractItem<ExpandableSubItem.ChildViewHolder> implements ISectionable<ExpandableSubItem.ChildViewHolder, ExpandableHeaderItem> {

    private String name;
    private String date;
    private String listName;
    private String id;
    ExpandableHeaderItem header;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ExpandableSubItem(SubItem invoiceSubItem, ExpandableHeaderItem expandableHeaderItem) {
        this.id = invoiceSubItem.getId();
        this.header = expandableHeaderItem;
        this.date = invoiceSubItem.date();
        this.name = invoiceSubItem.getName();
        this.listName = invoiceSubItem.getListNameEncoded();
        setDraggable(true);
    }

    /**
     * Called by the FlexibleAdapter when it wants to check if this item should be bound
     * again with new content.
     * <p>
     * You should return {@code true} whether you want this item will be updated because
     * its visual representations will change.
     * <p>
     * This method is called only if {@link FlexibleAdapter#setNotifyChangeOfUnfilteredItems(boolean)}
     * is enabled.
     * <p>Default value is {@code true}.</p>
     *
     * @param newItem The new item object with the new content
     * @return True will trigger a new binding to display new content, false if the content shown
     * is already the latest data.
     */
    /*@Override
    public boolean shouldNotifyChange(IFlexible newItem) {
        SubItem subItem = (SubItem) newItem;
        return !title.equals(subItem.getTitle()); // Should be bound again if title is different
    }*/
    @Override
    public int getLayoutRes() {
        return R.layout.recycler_name_list_item;
    }

    @Override
    public ChildViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChildViewHolder(view, (ExampleAdapter) adapter);
    }


    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ChildViewHolder holder, int position, List<Object> payloads) {
        //In case of searchText matches with Title or with an SimpleItem's field
        // this will be highlighted
        holder.mTitle.setText(getName());
        holder.mSubtitle.setText("" + getDate());
    }


    @Override
    public ExpandableHeaderItem getHeader() {
        return header;
    }

    @Override
    public void setHeader(ExpandableHeaderItem header) {

        this.header = header;
    }

    /**
     * Provide a reference to the views for each data item.
     * Complex data labels may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     */
    final class ChildViewHolder extends FlexibleViewHolder {
        TextView mTitle;
        TextView mSubtitle;
        RelativeLayout rowHeaderLayout;

        public ChildViewHolder(View view, ExampleAdapter adapter) {
            super(view, adapter);
            mTitle = view.findViewById(R.id.name);
            mSubtitle = view.findViewById(R.id.date);
            rowHeaderLayout = view.findViewById(R.id.main_item);


            rowHeaderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpandableSubItem expandableSubItem = (ExpandableSubItem) adapter.getItem(getAdapterPosition());
                    adapter.onItemClickListener.onItemClick(expandableSubItem.listName);

                }
            });
        }


        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
            AnimatorHelper.scaleAnimator(animators, itemView, 0f);
        }
    }

    @Override
    public String toString() {
        return "SubItem[" + super.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

}