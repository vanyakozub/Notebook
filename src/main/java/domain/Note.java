package domain;

/**
 * Configures note's data.
 */
public class Note {
    /**
     * Integer field which define identifier of note.
     */
    private Integer id;
    /**
     * String field which define note's header.
     */
    private String header;
    /**
     * String field which define name of user who posted the note.
     */
    private String name;
    /**
     * String field which define note's description.
     */
    private String description;
    /**
     * String field which define Email of user who posted the note.
     */
    private String email;

    /**
     *
     * @param id note's id
     * @param header note's header
     * @param name user's name
     * @param description note's description
     * @param email user's Email
     */
    public Note(final Integer id, final String header, final String name, final String description, final String email) {
        this.id = id;
        this.header = header;
        this.name = name;
        this.description = description;
        this.email = email;
    }

    /**
     *
     * @return note's id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id id to set
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     *
     * @return note's header
     */
    public String getHeader() {
        return header;
    }

    /**
     *
     * @param header header to set
     */
    public void setHeader(final String header) {
        this.header = header;
    }

    /**
     *
     * @return name of user who posted the note
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name user's name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     *
     * @return note's description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     *
     * @return user's Email
     */
    public String getEmail() {
        return email;
    }
    /**
     *
     * @param email user's Email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != getClass()) {
            return false;
        }
        Note note = (Note) o;
        if (this.header != note.header || this.email != note.email) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
