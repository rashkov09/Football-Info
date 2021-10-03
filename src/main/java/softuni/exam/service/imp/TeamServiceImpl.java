package softuni.exam.service.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.xmlDtos.TeamRootSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.TeamRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.TeamService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class TeamServiceImpl implements TeamService {
    private final static String TEAMS_FILE_PATH = "src/main/resources/files/xml/teams.xml";
    private final XmlParser xmlParser;
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validationUtils;
    private final FileUtil fileUtil;
    private final PictureService pictureService;

    public TeamServiceImpl(XmlParser xmlParser, TeamRepository teamRepository, ModelMapper modelMapper, ValidatorUtil validationUtils, FileUtil fileUtil, PictureService pictureService) {
        this.xmlParser = xmlParser;
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.validationUtils = validationUtils;
        this.fileUtil = fileUtil;
        this.pictureService = pictureService;
    }

    @Override
    public String importTeams() throws JAXBException, FileNotFoundException {
        TeamRootSeedDto teamRootSeedDto = xmlParser.fromFile(TEAMS_FILE_PATH,TeamRootSeedDto.class);
        StringBuilder builder = new StringBuilder();
        teamRootSeedDto.getTeams().forEach(teamSeedDto -> {
            boolean valid = validationUtils.isValid(teamSeedDto);
            if (valid){
                Team team = modelMapper.map(teamSeedDto,Team.class) ;
                Picture picture = pictureService.findPictureByUrl(teamSeedDto.getPicture().getUrl());
                if (picture != null){
                    team.setPicture(picture);
                    teamRepository.save(team);
                    builder.append(String.format("Successfully imported - %s",team.getName()));
                } else {
                    builder.append("Invalid team");
                }
            } else {
                builder.append("Invalid team");
            }
            builder.append(System.lineSeparator());
        });

        return builder.toString();
    }

    @Override
    public boolean areImported() {
        return teamRepository.count()> 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return fileUtil.readFile(TEAMS_FILE_PATH);
    }

    @Override
    public Team getTeam(String name) {
        return teamRepository.getByName(name);
    }
}
