package softuni.exam.service.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.xmlDtos.PictureRootSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class PictureServiceImpl implements PictureService {
    private final static String PICTURES_FILE_PATH = "src/main/resources/files/xml/pictures.xml";
    private final PictureRepository pictureRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validationUtils;
    private final FileUtil fileUtil;

    public PictureServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidatorUtil validationUtils, FileUtil fileUtil) {
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.fileUtil = fileUtil;
    }


    @Override
    public String importPictures() throws JAXBException, FileNotFoundException {
        PictureRootSeedDto pictureRootSeedDto = xmlParser.fromFile(PICTURES_FILE_PATH,PictureRootSeedDto.class);
        StringBuilder builder = new StringBuilder();

        pictureRootSeedDto.getPictures().forEach(pictureSeedDto -> {
            boolean valid = validationUtils.isValid(pictureSeedDto);

            if (valid){
                pictureRepository.save(modelMapper.map(pictureSeedDto, Picture.class));
                builder.append(String.format("Successfully imported picture - %s",pictureSeedDto.getUrl()));
            } else {
                builder.append("Invalid picture");
            }
            builder.append(System.lineSeparator());
        });

        return builder.toString();
    }

    @Override
    public boolean areImported() {

        return pictureRepository.count()>0  ;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {

        return fileUtil.readFile(PICTURES_FILE_PATH);
    }

    @Override
    public Picture findPictureByUrl(String url) {
        return pictureRepository.findPictureByUrl(url);
    }

}
