package com.github.bkhablenko.support

import org.assertj.core.api.AbstractThrowableAssert

inline fun <reified T : Throwable> AbstractThrowableAssert<*, *>.isInstanceOf(): AbstractThrowableAssert<*, *> =
    isInstanceOf(T::class.java)
