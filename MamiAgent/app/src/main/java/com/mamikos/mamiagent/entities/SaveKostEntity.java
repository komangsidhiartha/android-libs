package com.mamikos.mamiagent.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by foursixseven on 5/31/16.
 * Used for eventKost, newKost (this object used for input dataKost who want user set input as fast as he can)
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class SaveKostEntity implements Parcelable {
    private List<Integer> concernIds;
    private List<String> roomSize;
    private List<Integer> facProperty, facRoom, facBath;
    private PhotoUrlEntity photoUrl;
    private PhotoFormEntity photos;

    private String name, address, size, ownerName, ownerPhone, managerName, managerPhone,
            phone, officePhone, area, residentRemark, priceRemark, facRoomOther, facBathOther,
            facShare, facShareOther, facNear, facNearOther, facParking, paymentDuration,
            remarks, description, agentName, agentEmail, agentPhone, agentStatus, floor, deposit,
            roomTitle, priceTitleTime, areaCity, areaSubdistrict, statusKost, youtubeLink;
    private String subdistrict, city, province, inputAs,  ownerEmail, password, wifiSpeed="";

    private String inputSource;

    private double latitude, longitude, agentLatitude, agentLongitude;
    private int extend, price, photoId, animal, userId, gender, roomAvailable,
            roomCount, priceDaily, priceWeekly, priceMonthly, minMonth, withListrik, withoutListrik;
    private long priceYearly;

    private boolean agreeReview;
    private boolean checkin;

    private int selfiePhoto;

    public PhotoFormEntity getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoFormEntity photos) {
        this.photos = photos;
    }

    public void setWithoutListrik(int withoutListrik) {
        this.withoutListrik = withoutListrik;
    }

    public void setWithListrik(int withListrik) {
        this.withListrik = withListrik;
    }

    public int getWithoutListrik() {
        return withoutListrik;
    }

    public int getWithListrik() {
        return withListrik;
    }

    public void setWifiSpeed(String wifiSpeed) {
        this.wifiSpeed = wifiSpeed;
    }

    public String getWifiSpeed() {
        return wifiSpeed;
    }

    public void setMinMonth(int minMonth) {
        this.minMonth = minMonth;
    }

    public int getMinMonth() {
        return minMonth;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setAgentLat(double agentLat) {
        this.agentLatitude = agentLat;
    }

    public void setAgentLong(double agentLong) {
        this.agentLongitude = agentLong;
    }

    public double getAgentLong() {
        return agentLongitude;
    }

    public double getAgentLat() {
        return agentLatitude;
    }

    public void setSelfiePhoto(int selfiePhoto) {
        this.selfiePhoto = selfiePhoto;
    }

    public int getSelfiePhoto() {
        return selfiePhoto;
    }

    public String getInputSource() {
        return inputSource;
    }

    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    public String getAreaCity() {
        return areaCity;
    }

    public void setAreaCity(String areaCity) {
        this.areaCity = areaCity;
    }

    public String getAreaSubdistrict() {
        return areaSubdistrict;
    }

    public void setAreaSubdistrict(String areaSubdistrict) {
        this.areaSubdistrict = areaSubdistrict;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInputAs() {
        return inputAs;
    }

    public void setInputAs(String inputAs) {
        this.inputAs = inputAs;
    }

    public List<String> getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(List<String> roomSize) {
        this.roomSize = roomSize;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PhotoUrlEntity getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(PhotoUrlEntity photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getArea_city() {
        return areaCity;
    }

    public void setArea_city(String area_city) {
        this.areaCity = area_city;
    }

    public String getArea_subdistrict() {
        return areaSubdistrict;
    }

    public void setArea_subdistrict(String area_subdistrict) {
        this.areaSubdistrict = area_subdistrict;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public String getPriceTitleTime() {
        return priceTitleTime;
    }

    public void setPriceTitleTime(String priceTitleTime) {
        this.priceTitleTime = priceTitleTime;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getStatusKost() {
        return statusKost;
    }

    public void setStatusKost(String statusKost) {
        this.statusKost = statusKost;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getResidentRemark() {
        return residentRemark;
    }

    public void setResidentRemark(String residentRemark) {
        this.residentRemark = residentRemark;
    }

    public int getPriceDaily() {
        return priceDaily;
    }

    public void setPriceDaily(int priceDaily) {
        this.priceDaily = priceDaily;
    }

    public int getPriceWeekly() {
        return priceWeekly;
    }

    public void setPriceWeekly(int priceWeekly) {
        this.priceWeekly = priceWeekly;
    }

    public int getPriceMonthly() {
        return priceMonthly;
    }

    public void setPriceMonthly(int priceMonthly) {
        this.priceMonthly = priceMonthly;
    }

    public long getPriceYearly() {
        return priceYearly;
    }

    public void setPriceYearly(long priceYearly) {
        this.priceYearly = priceYearly;
    }

    public String getPriceRemark() {
        return priceRemark;
    }

    public void setPriceRemark(String priceRemark) {
        this.priceRemark = priceRemark;
    }

    public List<Integer> getFacProperty() {
        return facProperty;
    }

    public void setFacProperty(List<Integer> facProperty) {
        this.facProperty = facProperty;
    }

    public List<Integer> getFacRoom() {
        return facRoom;
    }

    public void setFacRoom(List<Integer> facRoom) {
        this.facRoom = facRoom;
    }

    public List<Integer> getFacBath() {
        return facBath;
    }

    public void setFacBath(List<Integer> facBath) {
        this.facBath = facBath;
    }

    public String getFacRoomOther() {
        return facRoomOther;
    }

    public void setFacRoomOther(String facRoomOther) {
        this.facRoomOther = facRoomOther;
    }

    public String getFacBathOther() {
        return facBathOther;
    }

    public void setFacBathOther(String facBathOther) {
        this.facBathOther = facBathOther;
    }

    public String getFacShare() {
        return facShare;
    }

    public void setFacShare(String facShare) {
        this.facShare = facShare;
    }

    public String getFacShareOther() {
        return facShareOther;
    }

    public void setFacShareOther(String facShareOther) {
        this.facShareOther = facShareOther;
    }

    public String getFacNear() {
        return facNear;
    }

    public void setFacNear(String facNear) {
        this.facNear = facNear;
    }

    public String getFacNearOther() {
        return facNearOther;
    }

    public void setFacNearOther(String facNearOther) {
        this.facNearOther = facNearOther;
    }

    public String getFacParking() {
        return facParking;
    }

    public void setFacParking(String facParking) {
        this.facParking = facParking;
    }

    public String getPaymentDuration() {
        return paymentDuration;
    }

    public void setPaymentDuration(String paymentDuration) {
        this.paymentDuration = paymentDuration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    @Override
    public String toString() {
        return "SaveKostEntity{" +
                "concernIds=" + concernIds +
                ", facProperty=" + facProperty +
                ", facRoom=" + facRoom +
                ", facBath=" + facBath +
                ", photoUrl=" + photoUrl +
                ", photos=" + photos +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", size='" + size + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", selfiePhoto='" + selfiePhoto + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerPhone='" + managerPhone + '\'' +
                ", phone='" + phone + '\'' +
                ", officePhone='" + officePhone + '\'' +
                ", area='" + area + '\'' +
                ", residentRemark='" + residentRemark + '\'' +
                ", priceRemark='" + priceRemark + '\'' +
                ", facRoomOther='" + facRoomOther + '\'' +
                ", facBathOther='" + facBathOther + '\'' +
                ", facShare='" + facShare + '\'' +
                ", facShareOther='" + facShareOther + '\'' +
                ", facNear='" + facNear + '\'' +
                ", facNearOther='" + facNearOther + '\'' +
                ", facParking='" + facParking + '\'' +
                ", paymentDuration='" + paymentDuration + '\'' +
                ", remarks='" + remarks + '\'' +
                ", description='" + description + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentEmail='" + agentEmail + '\'' +
                ", province='" + province + '\'' +
                ", agentPhone='" + agentPhone + '\'' +
                ", agentStatus='" + agentStatus + '\'' +
                ", floor='" + floor + '\'' +
                ", deposit='" + deposit + '\'' +
                ", roomTitle='" + roomTitle + '\'' +
                ", priceTitleTime='" + priceTitleTime + '\'' +
                ", areaCity='" + areaCity + '\'' +
                ", areaSubdistrict='" + areaSubdistrict + '\'' +
                ", statusKost='" + statusKost + '\'' +
                ", subdistrict='" + subdistrict + '\'' +
                ", city='" + city + '\'' +
                ", inputAs='" + inputAs + '\'' +
                ", roomSize='" + roomSize + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", password='" + password + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", agentLatitude=" + agentLatitude +
                ", agentLongitude=" + agentLongitude +
                ", extend=" + extend +
                ", price=" + price +
                ", photoId=" + photoId +
                ", animal=" + animal +
                ", userId=" + userId +
                ", gender=" + gender +
                ", roomAvailable=" + roomAvailable +
                ", roomCount=" + roomCount +
                ", priceDaily=" + priceDaily +
                ", priceWeekly=" + priceWeekly +
                ", priceMonthly=" + priceMonthly +
                ", priceYearly=" + priceYearly +
                ", agreeReview=" + agreeReview +
                ", checkin=" + checkin +
                ", minMonth=" + minMonth +
                ", wifiSpeed=" + wifiSpeed +
                ", inputSource=" + inputSource +
                '}';
    }

    public List<Integer> getConcernIds() {
        return concernIds;
    }

    public void setConcernIds(List<Integer> concernIds) {
        this.concernIds = concernIds;
    }

    public int getExtend() {
        return extend;
    }

    public void setExtend(int extend) {
        this.extend = extend;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getAnimal() {
        return animal;
    }

    public void setAnimal(int animal) {
        this.animal = animal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public int getRoomAvailable() {
        return roomAvailable;
    }

    public void setRoomAvailable(int roomAvailable) {
        this.roomAvailable = roomAvailable;
    }

    public boolean isAgreeReview() {
        return agreeReview;
    }

    public void setAgreeReview(boolean agreeReview) {
        this.agreeReview = agreeReview;
    }

    public SaveKostEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.concernIds);
        dest.writeList(this.facProperty);
        dest.writeList(this.facRoom);
        dest.writeList(this.facBath);
        dest.writeList(this.roomSize);
        dest.writeParcelable(this.photoUrl, flags);
        dest.writeParcelable(this.photos, flags);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.size);
        dest.writeString(this.ownerName);
        dest.writeInt(this.selfiePhoto);
        dest.writeString(this.ownerPhone);
        dest.writeString(this.managerName);
        dest.writeString(this.managerPhone);
        dest.writeString(this.phone);
        dest.writeString(this.officePhone);
        dest.writeString(this.area);
        dest.writeString(this.residentRemark);
        dest.writeString(this.priceRemark);
        dest.writeString(this.facRoomOther);
        dest.writeString(this.facBathOther);
        dest.writeString(this.facShare);
        dest.writeString(this.facShareOther);
        dest.writeString(this.facNear);
        dest.writeString(this.facNearOther);
        dest.writeString(this.facParking);
        dest.writeString(this.paymentDuration);
        dest.writeString(this.remarks);
        dest.writeString(this.description);
        dest.writeString(this.agentName);
        dest.writeString(this.agentEmail);
        dest.writeString(this.agentPhone);
        dest.writeString(this.agentStatus);
        dest.writeString(this.floor);
        dest.writeString(this.deposit);
        dest.writeString(this.roomTitle);
        dest.writeString(this.priceTitleTime);
        dest.writeString(this.areaCity);
        dest.writeString(this.areaSubdistrict);
        dest.writeString(this.statusKost);
        dest.writeString(this.subdistrict);
        dest.writeString(this.city);
        dest.writeString(this.inputAs);
        dest.writeString(this.ownerEmail);
        dest.writeString(this.password);
        dest.writeString(this.inputSource);
        dest.writeString(this.youtubeLink);
        dest.writeString(this.wifiSpeed);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.agentLatitude);
        dest.writeDouble(this.agentLongitude);
        dest.writeInt(this.roomCount);
        dest.writeInt(this.extend);
        dest.writeInt(this.price);
        dest.writeInt(this.photoId);
        dest.writeInt(this.animal);
        dest.writeInt(this.userId);
        dest.writeInt(this.gender);
        dest.writeInt(this.roomAvailable);
        dest.writeInt(this.priceDaily);
        dest.writeInt(this.priceWeekly);
        dest.writeInt(this.priceMonthly);
        dest.writeInt(this.minMonth);
        dest.writeLong(this.priceYearly);
        dest.writeByte(this.agreeReview ? (byte) 1 : (byte) 0);
        dest.writeByte(this.checkin ? (byte) 1 : (byte) 0);
    }

    protected SaveKostEntity(Parcel in) {
        this.concernIds = new ArrayList<Integer>();
        in.readList(this.concernIds, Integer.class.getClassLoader());
        this.facProperty = new ArrayList<Integer>();
        in.readList(this.facProperty, Integer.class.getClassLoader());
        this.facRoom = new ArrayList<Integer>();
        in.readList(this.facRoom, Integer.class.getClassLoader());
        this.facBath = new ArrayList<Integer>();
        in.readList(this.facBath, Integer.class.getClassLoader());
        this.roomSize = new ArrayList<String>();
        in.readList(this.roomSize, String.class.getClassLoader());
        this.photoUrl = in.readParcelable(PhotoUrlEntity.class.getClassLoader());
        this.photos = in.readParcelable(PhotoFormEntity.class.getClassLoader());
        this.name = in.readString();
        this.address = in.readString();
        this.size = in.readString();
        this.ownerName = in.readString();
        this.selfiePhoto = in.readInt();
        this.ownerPhone = in.readString();
        this.managerName = in.readString();
        this.managerPhone = in.readString();
        this.phone = in.readString();
        this.officePhone = in.readString();
        this.area = in.readString();
        this.residentRemark = in.readString();
        this.priceRemark = in.readString();
        this.facRoomOther = in.readString();
        this.facBathOther = in.readString();
        this.facShare = in.readString();
        this.facShareOther = in.readString();
        this.facNear = in.readString();
        this.facNearOther = in.readString();
        this.facParking = in.readString();
        this.paymentDuration = in.readString();
        this.remarks = in.readString();
        this.description = in.readString();
        this.agentName = in.readString();
        this.agentEmail = in.readString();
        this.agentPhone = in.readString();
        this.agentStatus = in.readString();
        this.floor = in.readString();
        this.deposit = in.readString();
        this.roomTitle = in.readString();
        this.priceTitleTime = in.readString();
        this.areaCity = in.readString();
        this.areaSubdistrict = in.readString();
        this.statusKost = in.readString();
        this.subdistrict = in.readString();
        this.city = in.readString();
        this.wifiSpeed = in.readString();
        this.inputAs = in.readString();
        this.ownerEmail = in.readString();
        this.password = in.readString();
        this.inputSource = in.readString();
        this.youtubeLink = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.agentLongitude = in.readDouble();
        this.agentLatitude = in.readDouble();
        this.roomCount = in.readInt();
        this.minMonth = in.readInt();
        this.extend = in.readInt();
        this.price = in.readInt();
        this.photoId = in.readInt();
        this.animal = in.readInt();
        this.userId = in.readInt();
        this.gender = in.readInt();
        this.roomAvailable = in.readInt();
        this.priceDaily = in.readInt();
        this.priceWeekly = in.readInt();
        this.priceMonthly = in.readInt();
        this.priceYearly = in.readLong();
        this.agreeReview = in.readByte() != 0;
        this.checkin = in.readByte() != 0;
    }

    public static final Creator<SaveKostEntity> CREATOR = new Creator<SaveKostEntity>() {
        @Override
        public SaveKostEntity createFromParcel(Parcel source) {
            return new SaveKostEntity(source);
        }

        @Override
        public SaveKostEntity[] newArray(int size) {
            return new SaveKostEntity[size];
        }
    };
}
