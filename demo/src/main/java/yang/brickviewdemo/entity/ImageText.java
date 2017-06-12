package yang.brickviewdemo.entity;

/**
 * author: Matthew Yang on 17/6/6
 * e-mail: yangtian@yy.com
 */

public class ImageText {

    private String url;
    private String content;

    public ImageText(String url, String content) {
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
