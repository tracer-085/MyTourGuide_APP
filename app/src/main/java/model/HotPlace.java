package model;

public class HotPlace {
    private String PlaceName;
    private String CityName;
    private String ProvinceName;
    private String CountryName;
    private Integer ImageUrl;

    public HotPlace(String placeName, String cityName, String provinceName, String countryName, Integer imageUrl) {
        PlaceName = placeName;
        CityName = cityName;
        ProvinceName = provinceName;
        CountryName = countryName;
        ImageUrl = imageUrl;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public Integer getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }
}
