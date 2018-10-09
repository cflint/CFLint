component{
public boolean function getAPIVersion(
    required numeric requestedAPIVersion
){
    return (variables.availableAPIVersions.find(arguments.requestedAPIVersion) > 0) ?
        arguments.requestedAPIVersion :
        variables.availableAPIVersions[1];
}
}