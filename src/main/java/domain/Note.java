package domain;

public class Note {
    private Integer id;
    private String header;
    private String name;
    private String description;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if( o == null || getClass() != getClass()) return false;
        Note note = (Note) o;
        if (this.id != note.id) return false;
        return true;
    }
    @Override
    public int hashCode() {
        return id;
    }
}
