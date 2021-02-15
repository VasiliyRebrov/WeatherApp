package com.weather.components

/***/
interface ItemTouchHelperAdapter {
    /**
     * Вызывается при выполнении перетаскивания.
     * Вызов происходит каждый раз, когда view-компонент списка меняет позицию.
     * */
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    /**
     *
     * */
    fun onItemDismiss(position: Int)

}