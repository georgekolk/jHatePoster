
public class PreparedPosts {
    private String directory;
    private String blogtype;
    private String blogname;
    private String key;
    private String tags;

    public PreparedPosts(String directory, String blogtype, String blogname, String key, String tags){
        this.directory = directory;
        this.blogtype = blogtype;
        this.blogname = blogname;
        this.key = key;
        this.tags = tags;
    }

    public String getDirectory(){
        return this.directory;
    }

    public String getBlogType(){
        return this.blogtype;
    }

    public String getBlogname(){
        return this.blogname;
    }

    public String getKey(){
        return this.key;
    }

    public String getTags(){
        return this.tags;
    }

    public void showAll(){
        System.out.println("directory: " + directory + " blogtype: " + blogtype + " blogname: " + blogname + " key: " + key + " tags: " + tags);
    }
}
