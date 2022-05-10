package com.apps.freeroadingdriver.model.responseModel

class Page {
    var meta_description: String? = null

    var pages_name: String? = null

    var pages_id: String? = null

    var subject: String? = null

    var language: String? = null

    var meta_title: String? = null

    var pages_desc: String? = null

    var meta_keyword: String? = null

    var pages_identifier: String? = null

    override fun toString(): String {
        return "ClassPojo [meta_description = $meta_description, pages_name = $pages_name, pages_id = $pages_id, subject = $subject, language = $language, meta_title = $meta_title, pages_desc = $pages_desc, meta_keyword = $meta_keyword, pages_identifier = $pages_identifier]"
    }
}
