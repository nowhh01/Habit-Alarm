package com.gl.habitalarm.enums

enum class EDay {
    Sunday,
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    override fun toString(): String {
        return super.toString().substring(0, 2).toUpperCase()
    }
}