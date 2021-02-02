package com.data.common

import com.data.model.City

fun City.toRegex() = component1().toString() + SEPARATOR + component2() +
        SEPARATOR + component3() + SEPARATOR + component4() + SEPARATOR + component5() +
        SEPARATOR + component6() + SEPARATOR + component7() + SEPARATOR + component8() +
        SEPARATOR + component9() + SEPARATOR + component10()