package br.com.knowledgeBase.api.knowledgebaseapi.data.contants;

public final class PathConstants {
    public static final String API_PATH = "/knowledgeBase-api";
    public static final String CATEGORY_ID_PARAM = "categoryId";
    public static final String SECTION_ID_PARAM = "sectionId";
    public static final String ID_PARAM = "id";
    public static final String SEARCH_PARAM = "param";

    public static final String SEARCH = "/search" + "/{" + SEARCH_PARAM + "}";
    public static final String LIST = "/list";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update" + "/{" + ID_PARAM + "}";
    public static final String DELETE = "/delete" + "/{" + ID_PARAM + "}";

    public static final String ARTICLE_PATH = API_PATH + "/articles";
    public static final String LIST_BY_CATEGORY = "/list-by-category" + "/{" + CATEGORY_ID_PARAM + "}";
    public static final String PRIVATE_LIST_BY_CATEGORY = "/list/category" + "/{" + CATEGORY_ID_PARAM + "}";
    public static final String LIST_BY_SECTION = "/list-by-section" + "/{" + SECTION_ID_PARAM + "}";
    public static final String PRIVATE_LIST_BY_SECTION = "/list/section" + "/{" + SECTION_ID_PARAM + "}";
    public static final String FIND_BY_ARTICLE_ID = "/article" + "/{" + ID_PARAM + "}";

    public static final String CATEGORY_PATH = API_PATH + "/categories";
    public static final String FIND_CATEGORY_BY_ID =  "/category" + "/{" + ID_PARAM + "}";
    
    public static final String SECTION_PATH = API_PATH + "/sections";
    public static final String FIND_SECTION_BY_ID = "/section" + "/{" + ID_PARAM + "}";
    public static final String FIND_SECTIONS_BY_CATEGORY_ID = LIST + "/{" + CATEGORY_ID_PARAM + "}";
    public static final String CREATE_SECTION = CREATE + "/category" + "/{" + CATEGORY_ID_PARAM + "}";
    public static final String UPDATE_SECTION = UPDATE + "/category" + "/{" + CATEGORY_ID_PARAM + "}";

    public static final String TAG_PATH = API_PATH + "/tags";
    public static final String USER_PATH = API_PATH + "/users";
}