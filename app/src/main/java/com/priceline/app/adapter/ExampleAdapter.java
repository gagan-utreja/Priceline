package com.priceline.app.adapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.priceline.app.OnItemClickListener;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;


/**
 * This is a custom implementation extending FlexibleAdapter. {@code AbstractFlexibleItem} is
 * used as most common Item for ALL view types.
 * <p>Binding is delegated via items (AutoMap), you <u>cannot</u> implement
 * {@code getItemViewType, onCreateViewHolder, onBindViewHolder}.</p>
 *
 * @see AbstractFlexibleItem
 */
public class ExampleAdapter extends FlexibleAdapter<AbstractFlexibleItem>  {

   public com.priceline.app.OnItemClickListener onItemClickListener;


    public ExampleAdapter(List<AbstractFlexibleItem> items, com.priceline.app.OnItemClickListener listeners) {
        // stableIds ? true = Items implement hashCode() so they can have stableIds!
        super(items, listeners, true);
       onItemClickListener = listeners;

        // In case you need a Handler, do this:
        // - Overrides the internal Handler with a custom callback that extends the internal one
        mHandler = new Handler(Looper.getMainLooper(), new MyHandlerCallback());
    }

    @Override
    public void updateDataSet(List<AbstractFlexibleItem> items, boolean animate) {
        // NOTE: To have views/items not changed, set them into "items" before passing the final
        // list to the Adapter.
        // Overwrite the list and fully notify the change, pass false to not animate changes.
        // Watch out! The original list must a copy.
        super.updateDataSet(items, animate);

        // onPostUpdate() will automatically be called at the end of the Asynchronous update
        // process. Manipulate the list inside that method only or you won't see the changes.
    }

    /*
     * You can override this method to define your own concept of "Empty".
     * This method is never called internally.
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }



    /*
     * Delegated via item objects. You should not implement this method!
     */
//	@Override
//	public int getItemViewType(int position) {
//		//Not implemented: METHOD A is used
//	}

    /*
     * Delegated via item objects. You should not implement this method!
     */
//	@Override
//	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//		// Not implemented: METHOD A is used
//	}

    /*
     * Delegated via item objects. You should not implement this method!
     */
//	@Override
//	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//		// Not implemented: METHOD A is used
//	}

    @Override
    public String onCreateBubbleText(int position) {
        if (position < getScrollableHeaders().size()) {
            return "Top";
        } else if (position >= getItemCount() - getScrollableFooters().size()) {
            return "Bottom";
        } else {
            position -= getScrollableHeaders().size() + 1;
        }
        // TODO FOR YOU: The basic value, usually, is the first letter
        // return getItem(position).getBubbleText(position);

        // For me the position is (position + 1):
        return super.onCreateBubbleText(position);
    }

 /*   @Override
    public boolean filter(AbstractFlexibleItem constraint) {
        boolean result = true;
        constraint.
        constraint.g
        if (constraint.is()) {
            result = this.electric == constraint.isElectric());
        }
        if (result && constraint.isPriceSet()) {
            result = this.price <= constraint.getPrice());
        }
        return result;
    }*/




    /**
     * Showcase to reuse the internal Handler.
     *
     * <b>IMPORTANT:</b> In order to preserve the internal calls, this custom Callback
     * <u>must</u> extends {@link HandlerCallback}
     * which implements {@link Handler.Callback},
     * therefore you <u>must</u> call {@code super().handleMessage(message)}.
     * <p>This handler can launch asynchronous tasks.</p>
     * If you catch the reserved "what", keep in mind that this code should be executed
     * <u>before</u> that task has been completed.
     * <p><b>Note:</b> numbers 0-9 are reserved for the Adapter, use others for new values.</p>
     */
    private class MyHandlerCallback extends HandlerCallback {
        @Override
        public boolean handleMessage(Message message) {
            boolean done = super.handleMessage(message);
            switch (message.what) {
                // Currently reserved (you don't need to check these numbers!)
                case 1: //async updateDataSet
                case 2: //async filterItems
                case 3: //confirm delete
                case 8: //onLoadMore remove progress item
                    return done;

                // Free to use, example:
                case 10:
                case 11:
                    return true;
            }
            return false;
        }
    }


}