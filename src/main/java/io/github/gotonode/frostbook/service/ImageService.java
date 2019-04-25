package io.github.gotonode.frostbook.service;

import io.github.gotonode.frostbook.domain.Image;
import io.github.gotonode.frostbook.domain.Profile;
import io.github.gotonode.frostbook.repository.ImageRepository;
import io.github.gotonode.frostbook.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProfileRepository profileRepository;

    public byte[] getImageContent(long id) {

        Image image = imageRepository.findById(id).orElse(null);

        if (image == null) {
            // Returns an empty byte array.
            return new byte[]{};
        }

        return image.getData();
    }

    @Transactional
    public Image create(MultipartFile file, String description, String handle) throws IOException {

        Profile profile = profileRepository.findProfileByHandle(handle);

        Image image = new Image();

        image.setDate(Date.from(Instant.now()));
        image.setData(file.getBytes());
        image.setContentType(file.getContentType().trim());
        image.setLength(file.getSize());
        image.setName(file.getOriginalFilename().trim());
        image.setDescription(description.trim());

        profile.getImages().add(image);

        imageRepository.save(image);

        profileRepository.save(profile);

        return image;
    }

    public Image findById(long id) {
        return imageRepository.findById(id).orElse(null);
    }
}
