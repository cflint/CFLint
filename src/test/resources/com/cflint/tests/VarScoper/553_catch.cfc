component {
    public string function renderPage( required any page, required any docTree, required boolean edit ){
        try {
            var renderedPage = renderTemplate(
                  template = "templates/#_getPageLayoutFile( arguments.page )#.cfm"
                , args     = { page = arguments.page, docTree=arguments.docTree, edit=edit }
                , helpers  = "/builders/html/helpers"
            );
        } catch( any e ) {
            e.additional.luceeDocsPageId = arguments.page.getid();
            rethrow;
        }
    }
}