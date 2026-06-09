package com.vrsalex.taskflow.domain.workscape.tag

import com.vrsalex.taskflow.domain.sync.repository.CrudRepository

interface TagRepository : CrudRepository<Tag, TagCreate, TagUpdate>