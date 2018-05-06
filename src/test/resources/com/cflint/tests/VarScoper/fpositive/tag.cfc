component{
    void function setCallerVars(data, row, getRow, columnNames){
        var currentRow=0;
        var rowData = getRow(data, row, columnNames);
        caller["currentRow"] = currentRow;
    }
}