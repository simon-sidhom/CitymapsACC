package com.citymaps.citymapsacc;

class CitymapsPhoto {
    private String imageUrl;

    public CitymapsPhoto(String imageUrl) {
        this.setImageUrl(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
