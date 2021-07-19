package com.priceline.app.view.expandable;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * This class will benefit of the already implemented methods (getter and setters) in
 * {@link AbstractFlexibleItem}.
 *
 * It is used as base item for all example models.
 */
public abstract class AbstractItem<VH extends FlexibleViewHolder>
        extends AbstractFlexibleItem<VH> {

    /**Nr
     * By default, recycler_header_item is hidden and not selectable
     */
    public AbstractItem() {
    }

}