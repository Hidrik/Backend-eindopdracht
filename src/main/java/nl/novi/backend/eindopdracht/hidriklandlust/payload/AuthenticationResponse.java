package nl.novi.backend.eindopdracht.hidriklandlust.payload;

public record AuthenticationResponse(String jwt) {

    public String getJwt() {
        return jwt;
    }

}