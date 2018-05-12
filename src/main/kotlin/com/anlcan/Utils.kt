package com.anlcan

/**
 * Created on 12.05.18.
 */
class CircularList<E>(override val size: Int,
                      private val internal: MutableList<E> = mutableListOf()) : MutableList<E> by internal{


}
