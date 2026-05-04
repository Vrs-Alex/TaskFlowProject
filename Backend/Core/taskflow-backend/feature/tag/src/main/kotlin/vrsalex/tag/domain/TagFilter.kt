package vrsalex.tag.domain

import vrsalex.core.value_object.Color

data class TagFilter(
    val name: String? = null,
    val nameExact: Boolean = false,
    val color: Color? = null
  )
