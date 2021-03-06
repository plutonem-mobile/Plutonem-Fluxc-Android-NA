package com.plutonem.android.fluxc.annotations.endpoint;

public class PlutonemEndpoint {
    private static final String PLUTONEM_REST_PREFIX = "http://3.15.14.1";
    private static final String PN_PREFIX_V1_1 = PLUTONEM_REST_PREFIX + "/rest/v1.1";
    private static final String PNCOM_PREFIX_V1_2 = PLUTONEM_REST_PREFIX + "/rest/v1.2";
    private static final String PN_PREFIX_V0 = PLUTONEM_REST_PREFIX;

    private final String mEndpoint;

    public PlutonemEndpoint(String endpoint) {
        mEndpoint = endpoint;
    }

    public PlutonemEndpoint(String endpoint, long id) {
        this(endpoint + id + "/");
    }

    public PlutonemEndpoint(String endpoint, String value) {
        this(endpoint + value + "/");
    }

    public String getEndpoint() {
        return mEndpoint;
    }

    public String getUrlV1_1() {
        return PN_PREFIX_V1_1 + mEndpoint;
    }

    public String getUrlV1_2() {
        return PNCOM_PREFIX_V1_2 + mEndpoint;
    }

    public String getUrlV0() {
        return PN_PREFIX_V0 + mEndpoint;
    }
}
