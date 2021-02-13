package com.weather

//для анимаций
interface ItemTouchHelperViewHolder {
    //вызывается каждый раз, когда состояние элемента меняется на drag или swipe
    fun onItemSelected()
   // вызывается при окончании перетаскивания view-компонента,
    // а также при завершении смахивания.
    fun onItemClear()

}