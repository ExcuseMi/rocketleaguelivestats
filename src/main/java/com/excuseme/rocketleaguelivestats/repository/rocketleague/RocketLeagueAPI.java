package com.excuseme.rocketleaguelivestats.repository.rocketleague;

import com.excuseme.rocketleaguelivestats.model.GamingSystem;
import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.StatisticsRepository;
import com.excuseme.rocketleaguelivestats.scanner.model.SessionData;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class RocketLeagueAPI implements StatisticsRepository {
    public static final String ISSUER_ID = "0";
    private final String APPSPOT_URL = "psyonix-rl.appspot.com";
    private final String LOGIN_PATH = "/auth/";
    private final String COMMAND_PATH = "/callproc105/";
    private final String SECRET_KEY = "dUe3SE4YsR8B0c30E6r7F2KqpZSbGiVx";
    private final String CALL_PROC_KEY = "pX9pn8F4JnBpoO8Aa219QC6N7g18FJ0F";
    private final String BUILDID = "-1543484724";
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private SessionData sessionData;
    private StatisticsParser statisticsParser = new StatisticsParser();

    public RocketLeagueAPI(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public String login(String playerName, String playerId, String platform, String buildId, String authCode) throws URISyntaxException, IOException {
        final URI uri = new URIBuilder().setScheme("https").setHost(APPSPOT_URL).setPath(LOGIN_PATH).build();

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Cache-Control", "no-cache");
        httpPost.setHeader("LoginSecretKey", SECRET_KEY);
        httpPost.setHeader("Environment", "Prod");
        httpPost.setHeader("User-Agent", "UE3-TA,UE3Ver(10897)");
        final List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("PlayerName", playerName));
        postParameters.add(new BasicNameValuePair("PlayerID", playerId));
        postParameters.add(new BasicNameValuePair("Platform", platform));
        postParameters.add(new BasicNameValuePair("BuildID", buildId));
        postParameters.add(new BasicNameValuePair("AuthCode", authCode));
        postParameters.add(new BasicNameValuePair("IssuerID", ISSUER_ID));
        httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
        final CloseableHttpResponse response = httpclient.execute(httpPost);
        final Header[] sessionIDs = response.getHeaders("SessionID");
        if (sessionIDs != null && sessionIDs.length == 1) {
            return sessionIDs[0].getValue();
        }
        throw new IllegalArgumentException("No session found");
    }

    private String doRequest(String sessionID, Command command) throws URISyntaxException, IOException {
        final URI uri = new URIBuilder().setScheme("https").setHost(APPSPOT_URL).setPath(COMMAND_PATH)
                .build();
        HttpPost httpPost = new HttpPost(uri);
        setHeaders(httpPost, sessionID);
        final List<NameValuePair> nameValuePairs = encodeCommand(command);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        final CloseableHttpResponse response = httpclient.execute(httpPost);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.getEntity().writeTo(outputStream);
        return outputStream.toString();
    }

    private void setHeaders(HttpPost httpPost, String sessionID) {
        httpPost.setHeader("Cache-Control", "no-cache");
        httpPost.setHeader("Environment", "Prod");
        httpPost.setHeader("SessionID", sessionID);
        httpPost.setHeader("CallProcKey", CALL_PROC_KEY);
        httpPost.setHeader("User-Agent", "UE3-TA,UE3Ver(10897)");
        httpPost.setHeader("Context-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("DBVersion", "00.03.0011-00.01.0011");
        httpPost.setHeader("DB", "BattleCars_Prod");
    }

    private List<NameValuePair> encodeCommand(Command command) {
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Proc[]", command.getName()));
        if (command.getParameters() != null) {
            for (int i = 0; i < command.getParameters().size(); i++) {
                nameValuePairs.add(new BasicNameValuePair(String.format("P%sP[]", i), command.getParameters().get(i)));
            }
        }
        return nameValuePairs;
    }

    @Override
    public Map<PlayerIdentifier, Statistics> find(List<PlayerIdentifier> playerIdentifiers) throws Exception {
        final String steamId = sessionData.getOwnPlayer().getSteamId();
        final String sessionId = login("", steamId, "Steam", sessionData.getBuildId(), sessionData.getAuthCode());
        final List<Command> commandList = playerIdentifiers.stream().map(p -> {
            if (GamingSystem.STEAM.equals(p.getGamingSystem())) {
                return new Command("GetPlayerSkillSteam", Arrays.asList(p.getPlayerId()), p);
            } else if (GamingSystem.PS4.equals(p.getGamingSystem())) {
                return new Command("GetPlayerSkillPS4", Arrays.asList(p.getPlayerId()), p);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        final Map<PlayerIdentifier, Statistics> playerIdentifierStatisticsHashMap = new HashMap<>();
        commandList.stream().forEach(c -> {
            try {
                final String result = doRequest(sessionId, c);
                Statistics statistics = parseResult(result);
                playerIdentifierStatisticsHashMap.put(c.getPlayerIdentifier(), statistics);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return playerIdentifierStatisticsHashMap;
    }

    private Statistics parseResult(String result) throws UnsupportedEncodingException {
        return statisticsParser.parse(result);
    }

    @Override
    public Statistics find(String playerId, String system) {
        return null;
    }


    @Override
    public String createUrl(String playerId, String system) {
        return null;
    }


    private class Command {
        private final String name;
        private final List<String> parameters;
        private PlayerIdentifier playerIdentifier;
        private Command(String name, List<String> parameters, PlayerIdentifier playerIdentifier) {
            this.name = name;
            this.parameters = parameters;
            this.playerIdentifier = playerIdentifier;
        }

        public String getName() {
            return name;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public PlayerIdentifier getPlayerIdentifier() {
            return playerIdentifier;
        }

    }
}
