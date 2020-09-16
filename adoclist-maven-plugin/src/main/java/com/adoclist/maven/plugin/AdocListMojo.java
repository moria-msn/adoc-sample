package com.adoclist.maven.plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.adoclist.maven.plugin.gson.Config;
import com.adoclist.maven.plugin.gson.Configs;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.orangesignal.csv.Csv;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.handlers.StringArrayListHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import static org.asciidoctor.Asciidoctor.Factory.create;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;
import org.asciidoctor.jruby.DirectoryWalker;

@Mojo(name = "adoclist", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class AdocListMojo extends AbstractMojo {

    @Parameter(property = "adoclist.config", required = false, defaultValue = "config.json")
    private String configFile;

    public AdocListMojo() {
    }

    @Override
    public void execute() throws MojoExecutionException {

        //configファイルの読み込み
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(configFile), "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new MojoExecutionException("configファイルがありません。" + e.getLocalizedMessage());
        }

        //configファイルのデシリアライズ
        JsonReader jsonReader = new JsonReader(inputStreamReader);
        Gson gson = new Gson();
        Configs configs = new Configs();
        try {
            configs = gson.fromJson(jsonReader, configs.getClass());
        } catch (JsonSyntaxException e) {
            throw new MojoExecutionException("configファイルのjson形式に不備があります。" + e.getLocalizedMessage());
        }

        /*asciidoc解析とcsv出力処理*/
        Asciidoctor asciidoctor = create();
        for (Config c : configs.getConfigs()) {

            List<String[]> csv = new ArrayList<>();

            //Asciidocファイルの検索
            DirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(c.getInputFile());
            List<File> asciidocFiles = directoryWalker.scan();

            for (File f : asciidocFiles) {

                //ファイル名で解析対象をフィルターする
                if (!f.getName().matches(c.getFilenameFilter())) continue;

                //csv各行の取り込み
                DocumentHeader header = asciidoctor.readDocumentHeader(new File(f.getAbsolutePath()));
                String[] value = new String[c.getElements().size()];
                for (int i = 0; i < c.getElements().size(); i++) {
                    try {
                        value[i] = header.getAttributes().get(c.getElements().get(i)).toString();
                    } catch (NullPointerException e) {
                        value[i] = "ERROR";
                        String message = String.format("\"%s\"に\"%s\"の属性が定義されていません。",
                                f.getAbsolutePath(), c.getElements().get(i));
                        getLog().warn(message);
                    }
                }
                csv.add(value);
            }

            //ソート
            csv.sort((s1, s2) -> -s2[c.getSortColumn() - 1].compareTo(s1[c.getSortColumn() - 1]));

            //csvヘッダの取り込み
            csv.add(0, c.getHeader().toArray(new String[c.getHeader().size()]));

            //csvの書き込み
            try {
                File csvFile = new File(c.getResultFile());
                new File(csvFile.getParent()).mkdirs();
                Csv.save(csv, csvFile, "UTF-8", new CsvConfig(), new StringArrayListHandler());
            } catch (IOException e) {
                throw new MojoExecutionException(e.getLocalizedMessage());
            }
        }

    }

    void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
