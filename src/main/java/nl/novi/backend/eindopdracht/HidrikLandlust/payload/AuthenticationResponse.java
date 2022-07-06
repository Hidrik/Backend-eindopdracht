package nl.novi.backend.eindopdracht.HidrikLandlust.payload;

public record AuthenticationResponse(String jwt) {

    public String getJwt() {
        return jwt;
    }

}