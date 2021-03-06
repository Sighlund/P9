package P9.model;

import javax.persistence.*;

/**
 * TextBlock implements an image which can be reused across different EObjects (engineering objects).
 * ImageBlock is a concrete implementation of the superclass ContentBlock.
 *
 * The class is mapped with Hibernate JPA. See: https://www.baeldung.com/jpa-entities
 * JPA inheritance mapping: https://www.baeldung.com/hibernate-inheritance
 */

@Entity
@Table(name = "image_block")
// Maps inheritance between content block and image block,
// using the join column "image_id" in the content block table
@PrimaryKeyJoinColumn(name = "content_block_id")
public class ImageBlock extends ContentBlock{

    // ----- Properties -----
    @Column(name = "image_path")
    private String imagePath;

    // ----- Constructor -----
    /**
     * Empty constructor
     */
    public ImageBlock() {
    }

    // ----- Getters and setters -----
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
