package com.priceline.app.view.expandable;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.priceline.app.R;
import com.priceline.app.adapter.ExampleAdapter;
import com.priceline.app.model.HeaderItem;
import com.priceline.app.model.SubItem;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.ExpandableViewHolder;

/**
 * This is an experiment to evaluate how a Section with header can also be expanded/collapsed.
 * <p>Here, it still benefits of the common fields declared in AbstractItem.</p>
 * It's important to note that, the ViewHolder must be specified in all &lt;diamond&gt; signature.
 */
public class ExpandableHeaderItem
        extends AbstractItem<ExpandableHeaderItem.ExpandableHeaderViewHolder>
        implements IExpandable<ExpandableHeaderItem.ExpandableHeaderViewHolder, ExpandableSubItem>, IHeader<ExpandableHeaderItem.ExpandableHeaderViewHolder> {


    public final boolean hasSubItems() {
        return mSubItems != null && mSubItems.size() > 0;
    }

    /* Flags for FlexibleAdapter */
    private boolean mExpanded = false;

    /* subItems list */
    private List<ExpandableSubItem> mSubItems = new ArrayList<>();
    private String id;
    private int count;
    private String type;

    public ExpandableHeaderItem(HeaderItem invoiceSubItem) {

        this.id = invoiceSubItem.getId();
        this.count = invoiceSubItem.getCount();
        this.type = invoiceSubItem.getType();

        //We start with header shown and expanded
        setHidden(false);
        setExpanded(true);
        //NOT selectable (otherwise ActionMode will be activated on long click)!
        setSelectable(false);

        for (SubItem oi : invoiceSubItem.getSubItems()) {
            this.mSubItems.add(new ExpandableSubItem(oi, this));
        }


    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    @Override
    public int getExpansionLevel() {
        return 0;
    }

    @Override
    public List<ExpandableSubItem> getSubItems() {
        return mSubItems;
    }


    public boolean removeSubItem(SubItem item) {
        return item != null && mSubItems.remove(item);
    }

    public boolean removeSubItem(int position) {
        if (mSubItems != null && position >= 0 && position < mSubItems.size()) {
            mSubItems.remove(position);
            return true;
        }
        return false;
    }

    public void addSubItem(ExpandableSubItem subItem) {
        if (mSubItems == null)
            mSubItems = new ArrayList<>();
        mSubItems.add(subItem);
    }

    public void addSubItem(int position, ExpandableSubItem subItem) {
        if (mSubItems != null && position >= 0 && position < mSubItems.size()) {
            mSubItems.add(position, subItem);
        } else
            addSubItem(subItem);
    }

    @Override
    public int getSpanSize(int spanCount, int position) {
        return spanCount;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recycler_header_item;
    }

    @Override
    public ExpandableHeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ExpandableHeaderViewHolder(view.getRootView(), (ExampleAdapter) adapter);
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, ExpandableHeaderViewHolder holder, int position, List<Object> payloads) {

        holder.mTitle.setText(getType());
        holder.mSubtitle.setText("" + getCount());
    }

    /**
     * Provide a reference to the views for each data item.
     * Complex data labels may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     */
    static class ExpandableHeaderViewHolder extends ExpandableViewHolder {

        TextView mTitle;
        TextView mSubtitle;
        RelativeLayout rowHeaderLayout;
        ImageView arrowImage;

        ExpandableHeaderViewHolder(View view, ExampleAdapter adapter) {
            super(view, adapter, true);//True for sticky
            mTitle = view.findViewById(R.id.type);
            mSubtitle = view.findViewById(R.id.count);
            rowHeaderLayout = view.findViewById(R.id.row_header_layout);
            arrowImage = view.findViewById(R.id.arrow);


            rowHeaderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.isExpanded(getPosition())) {
                        arrowImage.setImageResource(R.drawable.ic_arrow_down);
                        collapseView(getPosition());
                    } else {
                        arrowImage.setImageResource(R.drawable.ic_arrow_up);
                        expandView(getPosition());
                    }


                }
            });

            // Support for StaggeredGridLayoutManager
            setFullSpan(true);
        }

        /**
         * Allows to expand or collapse child views of this itemView when {@link View.OnClickListener}
         * event occurs on the entire view.
         * <p>This method returns always true; Extend with "return false" to Not expand or collapse
         * this ItemView onClick events.</p>
         *
         * @return always true, if not overridden
         * @since 5.0.0-b1
         */
        @Override
        protected boolean isViewExpandableOnClick() {
            return true;//default=true
        }

        /**
         * Allows to collapse child views of this ItemView when {@link View.OnClickListener}
         * event occurs on the entire view.
         * <p>This method returns always true; Extend with "return false" to Not collapse this
         * ItemView onClick events.</p>
         *
         * @return always true, if not overridden
         * @since 5.0.4
         */
        @Override
        protected boolean isViewCollapsibleOnClick() {
            return true;//default=true
        }

        /**
         * Allows to collapse child views of this ItemView when {@link View.OnLongClickListener}
         * event occurs on the entire view.
         * <p>This method returns always true; Extend with "return false" to Not collapse this
         * ItemView onLongClick events.</p>
         *
         * @return always true, if not overridden
         * @since 5.0.0-b1
         */
        @Override
        protected boolean isViewCollapsibleOnLongClick() {
            return true;//default=true
        }

        /**
         * Allows to notify change and rebound this itemView on expanding and collapsing events,
         * in order to update the content (so, user can decide to display the current expanding status).
         * <p>This method returns always false; Override with {@code "return true"} to trigger the
         * notification.</p>
         *
         * @return true to rebound the content of this itemView on expanding and collapsing events,
         * false to ignore the events
         * @see #expandView(int)
         * @see #collapseView(int)
         * @since 5.0.0-rc1
         */
        @Override
        protected boolean shouldNotifyParentOnClick() {
            return true;//default=false
        }

        /**
         * Expands or Collapses based on the current state.
         *
         * @see #shouldNotifyParentOnClick()
         * @see #expandView(int)
         * @see #collapseView(int)
         * @since 5.0.0-b1
         */
        @Override
        protected void toggleExpansion() {
            super.toggleExpansion(); //If overridden, you must call the super method
        }

        /**
         * Triggers expansion of this itemView.
         * <p>If {@link #shouldNotifyParentOnClick()} returns {@code true}, this view is rebound
         * with payload {@link Payload#EXPANDED}.</p>
         *
         * @see #shouldNotifyParentOnClick()
         * @since 5.0.0-b1
         */
        @Override
        protected void expandView(int position) {
            super.expandView(position); //If overridden, you must call the super method
            // Let's notify the item has been expanded. Note: from 5.0.0-rc1 the next line becomes
            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
            //if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

        /**
         * Triggers collapse of this itemView.
         * <p>If {@link #shouldNotifyParentOnClick()} returns {@code true}, this view is rebound
         * with payload {@link Payload#COLLAPSED}.</p>
         *
         * @see #shouldNotifyParentOnClick()
         * @since 5.0.0-b1
         */
        @Override
        protected void collapseView(int position) {
            super.collapseView(position); //If overridden, you must call the super method
            // Let's notify the item has been collapsed. Note: from 5.0.0-rc1 the next line becomes
            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
            //if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
        }

    }

    @Override
    public String toString() {
        return "ExpandableHeaderItem[" + super.toString() + "//SubItems" + mSubItems + "]";
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }


}