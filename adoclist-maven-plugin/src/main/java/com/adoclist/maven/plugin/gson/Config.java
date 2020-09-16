
package com.adoclist.maven.plugin.gson;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("inputFile")
    @Expose
    private String inputFile;
    @SerializedName("resultFile")
    @Expose
    private String resultFile;
    @SerializedName("sortColumn")
    @Expose
    private int sortColumn;
    @SerializedName("filenameFilter")
    @Expose
    private String filenameFilter;
    @SerializedName("header")
    @Expose
    private List<String> header = null;
    @SerializedName("elements")
    @Expose
    private List<String> elements = null;

    public String getInputFile() {
        return inputFile;
    }

    public String getResultFile() {
        return resultFile;
    }

    public int getSortColumn() {
        return sortColumn;
    }

    public String getFilenameFilter() {
        return filenameFilter;
    }

    public List<String> getHeader() {
        return header;
    }

    public List<String> getElements() {
        return elements;
    }



}
