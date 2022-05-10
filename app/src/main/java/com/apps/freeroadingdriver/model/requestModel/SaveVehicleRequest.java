package com.apps.freeroadingdriver.model.requestModel;
public class SaveVehicleRequest extends BaseRequest {
    private String is_ac;
    private String is_hybrid_electric;
    private String license_plate_no;
    private String no_of_door;
    private String no_of_seat;
    private String road_tax_no;
    private String state;
    private String upload_cert_reg_exp_date;
    private String vehicle_color;
    private String vehicle_insurance_no;
    private String vehicle_make_id;
    private String vehicle_model_id;
    private String vehicle_reg_no;
    private String vehicle_type_id;
    private String vehicle_wifi;
    private String vehicle_year;
    private String taxi_back_view;
    private String taxi_front_view;
    private String upload_motor_insurance_cert;
    private String liability_coverage_amount;
    private String collision_coverage_amount;
    private String comprehensive_coverage_amount;
    private String personal_injury_coverage_amount;
    private String uninsured_motorist_coverage_amount;

    public String getUpload_motor_insurance_cert() {
        return upload_motor_insurance_cert;
    }

    public void setUpload_motor_insurance_cert(String upload_motor_insurance_cert) {
        this.upload_motor_insurance_cert = upload_motor_insurance_cert;
    }

    public String getLiability_coverage_amount() {
        return liability_coverage_amount;
    }

    public void setLiability_coverage_amount(String liability_coverage_amount) {
        this.liability_coverage_amount = liability_coverage_amount;
    }

    public String getCollision_coverage_amount() {
        return collision_coverage_amount;
    }

    public void setCollision_coverage_amount(String collision_coverage_amount) {
        this.collision_coverage_amount = collision_coverage_amount;
    }

    public String getComprehensive_coverage_amount() {
        return comprehensive_coverage_amount;
    }

    public void setComprehensive_coverage_amount(String comprehensive_coverage_amount) {
        this.comprehensive_coverage_amount = comprehensive_coverage_amount;
    }

    public String getPersonal_injury_coverage_amount() {
        return personal_injury_coverage_amount;
    }

    public void setPersonal_injury_coverage_amount(String personal_injury_coverage_amount) {
        this.personal_injury_coverage_amount = personal_injury_coverage_amount;
    }

    public String getUninsured_motorist_coverage_amount() {
        return uninsured_motorist_coverage_amount;
    }

    public void setUninsured_motorist_coverage_amount(String uninsured_motorist_coverage_amount) {
        this.uninsured_motorist_coverage_amount = uninsured_motorist_coverage_amount;
    }

    public SaveVehicleRequest() {
        super(false, true, false, false);
    }

    public String getTaxi_back_view() {
        return taxi_back_view;
    }

    public void setTaxi_back_view(String taxi_back_view) {
        this.taxi_back_view = taxi_back_view;
    }

    public String getTaxi_front_view() {
        return taxi_front_view;
    }

    public void setTaxi_front_view(String taxi_front_view) {
        this.taxi_front_view = taxi_front_view;
    }

    public String getIs_ac() {
        return is_ac;
    }

    public void setIs_ac(String is_ac) {
        this.is_ac = is_ac;
    }

    public String getIs_hybrid_electric() {
        return is_hybrid_electric;
    }

    public void setIs_hybrid_electric(String is_hybrid_electric) {
        this.is_hybrid_electric = is_hybrid_electric;
    }


    public String getLicense_plate_no() {
        return license_plate_no;
    }

    public void setLicense_plate_no(String license_plate_no) {
        this.license_plate_no = license_plate_no;
    }

    public String getNo_of_door() {
        return no_of_door;
    }

    public void setNo_of_door(String no_of_door) {
        this.no_of_door = no_of_door;
    }

    public String getNo_of_seat() {
        return no_of_seat;
    }

    public void setNo_of_seat(String no_of_seat) {
        this.no_of_seat = no_of_seat;
    }

    public String getRoad_tax_no() {
        return road_tax_no;
    }

    public void setRoad_tax_no(String road_tax_no) {
        this.road_tax_no = road_tax_no;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpload_cert_reg_exp_date() {
        return upload_cert_reg_exp_date;
    }

    public void setUpload_cert_reg_exp_date(String upload_cert_reg_exp_date) {
        this.upload_cert_reg_exp_date = upload_cert_reg_exp_date;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getVehicle_insurance_no() {
        return vehicle_insurance_no;
    }

    public void setVehicle_insurance_no(String vehicle_insurance_no) {
        this.vehicle_insurance_no = vehicle_insurance_no;
    }

    public String getVehicle_make_id() {
        return vehicle_make_id;
    }

    public void setVehicle_make_id(String vehicle_make_id) {
        this.vehicle_make_id = vehicle_make_id;
    }

    public String getVehicle_model_id() {
        return vehicle_model_id;
    }

    public void setVehicle_model_id(String vehicle_model_id) {
        this.vehicle_model_id = vehicle_model_id;
    }

    public String getVehicle_reg_no() {
        return vehicle_reg_no;
    }

    public void setVehicle_reg_no(String vehicle_reg_no) {
        this.vehicle_reg_no = vehicle_reg_no;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getVehicle_wifi() {
        return vehicle_wifi;
    }

    public void setVehicle_wifi(String vehicle_wifi) {
        this.vehicle_wifi = vehicle_wifi;
    }

    public String getVehicle_year() {
        return vehicle_year;
    }

    public void setVehicle_year(String vehicle_year) {
        this.vehicle_year = vehicle_year;
    }
}
