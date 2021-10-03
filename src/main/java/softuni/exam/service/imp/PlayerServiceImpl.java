package softuni.exam.service.imp;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.jsonDtos.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.service.PictureService;
import softuni.exam.service.PlayerService;
import softuni.exam.service.TeamService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidatorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final static String PLAYERS_FILE_PATH = "src/main/resources/files/json/players.json";
    private final PlayerRepository playerRepository;
    private final PictureService pictureService;
    private final TeamService teamService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;
    private final FileUtil fileUtil;

    public PlayerServiceImpl(PlayerRepository playerRepository, PictureService pictureService, TeamService teamService, Gson gson, ModelMapper modelMapper, ValidatorUtil validatorUtil, FileUtil fileUtil) {
        this.playerRepository = playerRepository;
        this.pictureService = pictureService;
        this.teamService = teamService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.fileUtil = fileUtil;
    }


    @Override
    public String importPlayers() throws IOException {
        StringBuilder builder = new StringBuilder();
        PlayerSeedDto[] playerSeedDtos = gson.fromJson(fileUtil.readFile(PLAYERS_FILE_PATH), PlayerSeedDto[].class);
        Arrays.stream(playerSeedDtos).forEach(playerSeedDto -> {
            Picture picture = pictureService.getPicture(playerSeedDto.getPicture().getUrl());
            Team team =  teamService.getTeam(playerSeedDto.getTeam().getName());
            boolean valid = validatorUtil.isValid(playerSeedDto);
            if(valid && picture != null && team != null){
                Player player = modelMapper.map(playerSeedDto, Player.class);
                player.setPicture(picture);
                player.setTeam(team);
                playerRepository.save(player);
                builder.append(String.format("Successfully imported player %s",playerSeedDto.getFirstName()+" "+playerSeedDto.getLastName()));
            } else {
                builder.append("Invalid player");
            }
        });
        return builder.toString();
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() >0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return fileUtil.readFile(PLAYERS_FILE_PATH);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        StringBuilder builder = new StringBuilder();
        BigDecimal salary = new BigDecimal(100000);
        playerRepository.findAllBySalaryAfter(salary).stream().sorted((p,p1)-> p1.getSalary().compareTo(p.getSalary())).forEach(player -> {
            builder.append(String.format("Player name: %s%n     Number: %d%n     Salary: %.2f%n     Team: %s%n",
                    player.getFirstName()+" "+player.getLastName(), player.getNumber(), player.getSalary(), player.getTeam().getName()));
        });
        return builder.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder builder = new StringBuilder();
        String name = "North Hub";
        builder.append("Team: North Hub").append(System.lineSeparator());

        playerRepository.findPlayersByTeamName(name).forEach(player -> {
            builder.append(String.format("Player name: %s  - %s%nNumber: %d%n",player.getFirstName()+" "+player.getLastName(),player.getPosition().name(),player.getNumber()));
        });
        return builder.toString();
    }
}
