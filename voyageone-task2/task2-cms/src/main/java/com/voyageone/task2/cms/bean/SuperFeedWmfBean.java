package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjl on 2016/12/5.
 */
public class SuperFeedWmfBean extends SuperFeedBean {

    private String sku;

    private String category;

    private String price;

    private String specialPrice;

    private String md5;

    private Integer updateflag;

    private String entityId;

    private String store;

    private String type;

    private String attributeSet;

    private String status;

    private String visibility;

    private String taxClassId;

    private String image;

    private String smallImage;

    private String thumbnail;

    private String mediaImage;

    private String mediaLable;

    private String mediaPosition;

    private String mediaIsDisabled;

    private String name;

    private String marke;

    private String sapStatus;

    private String metaTitle;

    private String description;

    private String produktart;

    private String metaKeyword;

    private String shortDescription;

    private String produktart2;

    private String metaDescription;

    private String kollektion;

    private String specialFromDate;

    private String plShortText;

    private String artikelanzahl;

    private String specialToDate;

    private String lieferumfang;

    private String ean;

    private String material;

    private String urlKey;

    private String materialeigenschaft;

    private String nebenmaterial;

    private String produkteigenschaft;

    private String madeInGermany;

    private String induktionseignung;

    private String plImageOrientation;

    private String lieferlandAusschluss;

    private String herdart;

    private String simpleMsrp;

    private String countryOfManufacture;

    private String temperatureignung;

    private String simpleMsrpFromDate;

    private String hitzebestaendigkeit;

    private String simpleMsrpToDate;

    private String highlight1;

    private String deckelart;

    private String highlight2;

    private String messerart;

    private String highlight3;

    private String eignungAnzTassenEier;

    private String highlight4;

    private String masseLaengeInCm;

    private String highlight5;

    private String masseBreiteInCm;

    private String asin;

    private String masseHoeheInCm;

    private String masseKlingenlaengeInCm;

    private String masseDmInCm;

    private String masseDmHerdplattengr;

    private String masseFassvermInL;

    private String masseFuellmengeInG;

    private String masseNettogewichtInG;

    private String masseBruttogewichtInG;

    private String umdrehungenProMinute;

    private String leistungInW;

    private String leistungInV;

    private String leistungInHz;

    private String farbbezeichnungDerKollektion;

    private String farbe;

    private String garantie;

    private String pflege;

    private String altersgruppe;

    private String designer;

    private String themenMotive;

    private String designpreis;

    private String testauszeichnung;

    private String kollektionstext;

    private String variantenzugehoerigkeit;

    private String masseVolumenInL;

    private String masseUmdrehungenInUMin;

    private String masseUmdrehungenInKmH;

    private String masseLeistungInW;

    private String masseLeistungInV;

    private String masseLeistungInHz;

    private String verpackungHoeheInCm;

    private String verpackungBreiteInCm;

    private String verpackungTiefeInCm;

    private String nameTestauszeichnung;

    private String induktionsfaehigkeit;

    private String temperaturbestaendigkeitKorpu;

    private String temperaturbestaendigkeitDecke;

    private String hitzebestaendigBis;

    private String herstellungshinweis;

    private String anzahlTeileDp;

    private String masseDurchmesserInCmDp;

    private String masseFassungsvermoegenLDp;

    private String video1;

    private String video2;

    private String video3;

    private String video4;

    private String categoryValue;

    private String itemISize;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice == null ? null : specialPrice.trim();
    }

    public String getMd5() {
        Set<String> noMd5Fields = new HashSet<>();
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");
        return  beanToMd5(this,noMd5Fields);
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public Integer getUpdateflag() {
        return updateflag;
    }

    public void setUpdateflag(Integer updateflag) {
        this.updateflag = updateflag;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId == null ? null : entityId.trim();
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store == null ? null : store.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(String attributeSet) {
        this.attributeSet = attributeSet == null ? null : attributeSet.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility == null ? null : visibility.trim();
    }

    public String getTaxClassId() {
        return taxClassId;
    }

    public void setTaxClassId(String taxClassId) {
        this.taxClassId = taxClassId == null ? null : taxClassId.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage == null ? null : smallImage.trim();
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail == null ? null : thumbnail.trim();
    }

    public String getMediaImage() {
        return mediaImage;
    }

    public void setMediaImage(String mediaImage) {
        this.mediaImage = mediaImage == null ? null : mediaImage.trim();
    }

    public String getMediaLable() {
        return mediaLable;
    }

    public void setMediaLable(String mediaLable) {
        this.mediaLable = mediaLable == null ? null : mediaLable.trim();
    }

    public String getMediaPosition() {
        return mediaPosition;
    }

    public void setMediaPosition(String mediaPosition) {
        this.mediaPosition = mediaPosition == null ? null : mediaPosition.trim();
    }

    public String getMediaIsDisabled() {
        return mediaIsDisabled;
    }

    public void setMediaIsDisabled(String mediaIsDisabled) {
        this.mediaIsDisabled = mediaIsDisabled == null ? null : mediaIsDisabled.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke == null ? null : marke.trim();
    }

    public String getSapStatus() {
        return sapStatus;
    }

    public void setSapStatus(String sapStatus) {
        this.sapStatus = sapStatus == null ? null : sapStatus.trim();
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle == null ? null : metaTitle.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getProduktart() {
        return produktart;
    }

    public void setProduktart(String produktart) {
        this.produktart = produktart == null ? null : produktart.trim();
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword == null ? null : metaKeyword.trim();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription == null ? null : shortDescription.trim();
    }

    public String getProduktart2() {
        return produktart2;
    }

    public void setProduktart2(String produktart2) {
        this.produktart2 = produktart2 == null ? null : produktart2.trim();
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription == null ? null : metaDescription.trim();
    }

    public String getKollektion() {
        return kollektion;
    }

    public void setKollektion(String kollektion) {
        this.kollektion = kollektion == null ? null : kollektion.trim();
    }

    public String getSpecialFromDate() {
        return specialFromDate;
    }

    public void setSpecialFromDate(String specialFromDate) {
        this.specialFromDate = specialFromDate == null ? null : specialFromDate.trim();
    }

    public String getPlShortText() {
        return plShortText;
    }

    public void setPlShortText(String plShortText) {
        this.plShortText = plShortText == null ? null : plShortText.trim();
    }

    public String getArtikelanzahl() {
        return artikelanzahl;
    }

    public void setArtikelanzahl(String artikelanzahl) {
        this.artikelanzahl = artikelanzahl == null ? null : artikelanzahl.trim();
    }

    public String getSpecialToDate() {
        return specialToDate;
    }

    public void setSpecialToDate(String specialToDate) {
        this.specialToDate = specialToDate == null ? null : specialToDate.trim();
    }

    public String getLieferumfang() {
        return lieferumfang;
    }

    public void setLieferumfang(String lieferumfang) {
        this.lieferumfang = lieferumfang == null ? null : lieferumfang.trim();
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean == null ? null : ean.trim();
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material == null ? null : material.trim();
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey == null ? null : urlKey.trim();
    }

    public String getMaterialeigenschaft() {
        return materialeigenschaft;
    }

    public void setMaterialeigenschaft(String materialeigenschaft) {
        this.materialeigenschaft = materialeigenschaft == null ? null : materialeigenschaft.trim();
    }

    public String getNebenmaterial() {
        return nebenmaterial;
    }

    public void setNebenmaterial(String nebenmaterial) {
        this.nebenmaterial = nebenmaterial == null ? null : nebenmaterial.trim();
    }

    public String getProdukteigenschaft() {
        return produkteigenschaft;
    }

    public void setProdukteigenschaft(String produkteigenschaft) {
        this.produkteigenschaft = produkteigenschaft == null ? null : produkteigenschaft.trim();
    }

    public String getMadeInGermany() {
        return madeInGermany;
    }

    public void setMadeInGermany(String madeInGermany) {
        this.madeInGermany = madeInGermany == null ? null : madeInGermany.trim();
    }

    public String getInduktionseignung() {
        return induktionseignung;
    }

    public void setInduktionseignung(String induktionseignung) {
        this.induktionseignung = induktionseignung == null ? null : induktionseignung.trim();
    }

    public String getPlImageOrientation() {
        return plImageOrientation;
    }

    public void setPlImageOrientation(String plImageOrientation) {
        this.plImageOrientation = plImageOrientation == null ? null : plImageOrientation.trim();
    }

    public String getLieferlandAusschluss() {
        return lieferlandAusschluss;
    }

    public void setLieferlandAusschluss(String lieferlandAusschluss) {
        this.lieferlandAusschluss = lieferlandAusschluss == null ? null : lieferlandAusschluss.trim();
    }

    public String getHerdart() {
        return herdart;
    }

    public void setHerdart(String herdart) {
        this.herdart = herdart == null ? null : herdart.trim();
    }

    public String getSimpleMsrp() {
        return simpleMsrp;
    }

    public void setSimpleMsrp(String simpleMsrp) {
        this.simpleMsrp = simpleMsrp == null ? null : simpleMsrp.trim();
    }

    public String getCountryOfManufacture() {
        return countryOfManufacture;
    }

    public void setCountryOfManufacture(String countryOfManufacture) {
        this.countryOfManufacture = countryOfManufacture == null ? null : countryOfManufacture.trim();
    }

    public String getTemperatureignung() {
        return temperatureignung;
    }

    public void setTemperatureignung(String temperatureignung) {
        this.temperatureignung = temperatureignung == null ? null : temperatureignung.trim();
    }

    public String getSimpleMsrpFromDate() {
        return simpleMsrpFromDate;
    }

    public void setSimpleMsrpFromDate(String simpleMsrpFromDate) {
        this.simpleMsrpFromDate = simpleMsrpFromDate == null ? null : simpleMsrpFromDate.trim();
    }

    public String getHitzebestaendigkeit() {
        return hitzebestaendigkeit;
    }

    public void setHitzebestaendigkeit(String hitzebestaendigkeit) {
        this.hitzebestaendigkeit = hitzebestaendigkeit == null ? null : hitzebestaendigkeit.trim();
    }

    public String getSimpleMsrpToDate() {
        return simpleMsrpToDate;
    }

    public void setSimpleMsrpToDate(String simpleMsrpToDate) {
        this.simpleMsrpToDate = simpleMsrpToDate == null ? null : simpleMsrpToDate.trim();
    }

    public String getHighlight1() {
        return highlight1;
    }

    public void setHighlight1(String highlight1) {
        this.highlight1 = highlight1 == null ? null : highlight1.trim();
    }

    public String getDeckelart() {
        return deckelart;
    }

    public void setDeckelart(String deckelart) {
        this.deckelart = deckelart == null ? null : deckelart.trim();
    }

    public String getHighlight2() {
        return highlight2;
    }

    public void setHighlight2(String highlight2) {
        this.highlight2 = highlight2 == null ? null : highlight2.trim();
    }

    public String getMesserart() {
        return messerart;
    }

    public void setMesserart(String messerart) {
        this.messerart = messerart == null ? null : messerart.trim();
    }

    public String getHighlight3() {
        return highlight3;
    }

    public void setHighlight3(String highlight3) {
        this.highlight3 = highlight3 == null ? null : highlight3.trim();
    }

    public String getEignungAnzTassenEier() {
        return eignungAnzTassenEier;
    }

    public void setEignungAnzTassenEier(String eignungAnzTassenEier) {
        this.eignungAnzTassenEier = eignungAnzTassenEier == null ? null : eignungAnzTassenEier.trim();
    }

    public String getHighlight4() {
        return highlight4;
    }

    public void setHighlight4(String highlight4) {
        this.highlight4 = highlight4 == null ? null : highlight4.trim();
    }

    public String getMasseLaengeInCm() {
        return masseLaengeInCm;
    }

    public void setMasseLaengeInCm(String masseLaengeInCm) {
        this.masseLaengeInCm = masseLaengeInCm == null ? null : masseLaengeInCm.trim();
    }

    public String getHighlight5() {
        return highlight5;
    }

    public void setHighlight5(String highlight5) {
        this.highlight5 = highlight5 == null ? null : highlight5.trim();
    }

    public String getMasseBreiteInCm() {
        return masseBreiteInCm;
    }

    public void setMasseBreiteInCm(String masseBreiteInCm) {
        this.masseBreiteInCm = masseBreiteInCm == null ? null : masseBreiteInCm.trim();
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getMasseHoeheInCm() {
        return masseHoeheInCm;
    }

    public void setMasseHoeheInCm(String masseHoeheInCm) {
        this.masseHoeheInCm = masseHoeheInCm == null ? null : masseHoeheInCm.trim();
    }

    public String getMasseKlingenlaengeInCm() {
        return masseKlingenlaengeInCm;
    }

    public void setMasseKlingenlaengeInCm(String masseKlingenlaengeInCm) {
        this.masseKlingenlaengeInCm = masseKlingenlaengeInCm == null ? null : masseKlingenlaengeInCm.trim();
    }

    public String getMasseDmInCm() {
        return masseDmInCm;
    }

    public void setMasseDmInCm(String masseDmInCm) {
        this.masseDmInCm = masseDmInCm == null ? null : masseDmInCm.trim();
    }

    public String getMasseDmHerdplattengr() {
        return masseDmHerdplattengr;
    }

    public void setMasseDmHerdplattengr(String masseDmHerdplattengr) {
        this.masseDmHerdplattengr = masseDmHerdplattengr == null ? null : masseDmHerdplattengr.trim();
    }

    public String getMasseFassvermInL() {
        return masseFassvermInL;
    }

    public void setMasseFassvermInL(String masseFassvermInL) {
        this.masseFassvermInL = masseFassvermInL == null ? null : masseFassvermInL.trim();
    }

    public String getMasseFuellmengeInG() {
        return masseFuellmengeInG;
    }

    public void setMasseFuellmengeInG(String masseFuellmengeInG) {
        this.masseFuellmengeInG = masseFuellmengeInG == null ? null : masseFuellmengeInG.trim();
    }

    public String getMasseNettogewichtInG() {
        return masseNettogewichtInG;
    }

    public void setMasseNettogewichtInG(String masseNettogewichtInG) {
        this.masseNettogewichtInG = masseNettogewichtInG == null ? null : masseNettogewichtInG.trim();
    }

    public String getMasseBruttogewichtInG() {
        return masseBruttogewichtInG;
    }

    public void setMasseBruttogewichtInG(String masseBruttogewichtInG) {
        this.masseBruttogewichtInG = masseBruttogewichtInG == null ? null : masseBruttogewichtInG.trim();
    }

    public String getUmdrehungenProMinute() {
        return umdrehungenProMinute;
    }

    public void setUmdrehungenProMinute(String umdrehungenProMinute) {
        this.umdrehungenProMinute = umdrehungenProMinute == null ? null : umdrehungenProMinute.trim();
    }

    public String getLeistungInW() {
        return leistungInW;
    }

    public void setLeistungInW(String leistungInW) {
        this.leistungInW = leistungInW == null ? null : leistungInW.trim();
    }

    public String getLeistungInV() {
        return leistungInV;
    }

    public void setLeistungInV(String leistungInV) {
        this.leistungInV = leistungInV == null ? null : leistungInV.trim();
    }

    public String getLeistungInHz() {
        return leistungInHz;
    }

    public void setLeistungInHz(String leistungInHz) {
        this.leistungInHz = leistungInHz == null ? null : leistungInHz.trim();
    }

    public String getFarbbezeichnungDerKollektion() {
        return farbbezeichnungDerKollektion;
    }

    public void setFarbbezeichnungDerKollektion(String farbbezeichnungDerKollektion) {
        this.farbbezeichnungDerKollektion = farbbezeichnungDerKollektion == null ? null : farbbezeichnungDerKollektion.trim();
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe == null ? null : farbe.trim();
    }

    public String getGarantie() {
        return garantie;
    }

    public void setGarantie(String garantie) {
        this.garantie = garantie == null ? null : garantie.trim();
    }

    public String getPflege() {
        return pflege;
    }

    public void setPflege(String pflege) {
        this.pflege = pflege == null ? null : pflege.trim();
    }

    public String getAltersgruppe() {
        return altersgruppe;
    }

    public void setAltersgruppe(String altersgruppe) {
        this.altersgruppe = altersgruppe == null ? null : altersgruppe.trim();
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer == null ? null : designer.trim();
    }

    public String getThemenMotive() {
        return themenMotive;
    }

    public void setThemenMotive(String themenMotive) {
        this.themenMotive = themenMotive == null ? null : themenMotive.trim();
    }

    public String getDesignpreis() {
        return designpreis;
    }

    public void setDesignpreis(String designpreis) {
        this.designpreis = designpreis == null ? null : designpreis.trim();
    }

    public String getTestauszeichnung() {
        return testauszeichnung;
    }

    public void setTestauszeichnung(String testauszeichnung) {
        this.testauszeichnung = testauszeichnung == null ? null : testauszeichnung.trim();
    }

    public String getKollektionstext() {
        return kollektionstext;
    }

    public void setKollektionstext(String kollektionstext) {
        this.kollektionstext = kollektionstext == null ? null : kollektionstext.trim();
    }

    public String getVariantenzugehoerigkeit() {
        return variantenzugehoerigkeit;
    }

    public void setVariantenzugehoerigkeit(String variantenzugehoerigkeit) {
        this.variantenzugehoerigkeit = variantenzugehoerigkeit == null ? null : variantenzugehoerigkeit.trim();
    }

    public String getMasseVolumenInL() {
        return masseVolumenInL;
    }

    public void setMasseVolumenInL(String masseVolumenInL) {
        this.masseVolumenInL = masseVolumenInL == null ? null : masseVolumenInL.trim();
    }

    public String getMasseUmdrehungenInUMin() {
        return masseUmdrehungenInUMin;
    }

    public void setMasseUmdrehungenInUMin(String masseUmdrehungenInUMin) {
        this.masseUmdrehungenInUMin = masseUmdrehungenInUMin == null ? null : masseUmdrehungenInUMin.trim();
    }

    public String getMasseUmdrehungenInKmH() {
        return masseUmdrehungenInKmH;
    }

    public void setMasseUmdrehungenInKmH(String masseUmdrehungenInKmH) {
        this.masseUmdrehungenInKmH = masseUmdrehungenInKmH == null ? null : masseUmdrehungenInKmH.trim();
    }

    public String getMasseLeistungInW() {
        return masseLeistungInW;
    }

    public void setMasseLeistungInW(String masseLeistungInW) {
        this.masseLeistungInW = masseLeistungInW == null ? null : masseLeistungInW.trim();
    }

    public String getMasseLeistungInV() {
        return masseLeistungInV;
    }

    public void setMasseLeistungInV(String masseLeistungInV) {
        this.masseLeistungInV = masseLeistungInV == null ? null : masseLeistungInV.trim();
    }

    public String getMasseLeistungInHz() {
        return masseLeistungInHz;
    }

    public void setMasseLeistungInHz(String masseLeistungInHz) {
        this.masseLeistungInHz = masseLeistungInHz == null ? null : masseLeistungInHz.trim();
    }

    public String getVerpackungHoeheInCm() {
        return verpackungHoeheInCm;
    }

    public void setVerpackungHoeheInCm(String verpackungHoeheInCm) {
        this.verpackungHoeheInCm = verpackungHoeheInCm == null ? null : verpackungHoeheInCm.trim();
    }

    public String getVerpackungBreiteInCm() {
        return verpackungBreiteInCm;
    }

    public void setVerpackungBreiteInCm(String verpackungBreiteInCm) {
        this.verpackungBreiteInCm = verpackungBreiteInCm == null ? null : verpackungBreiteInCm.trim();
    }

    public String getVerpackungTiefeInCm() {
        return verpackungTiefeInCm;
    }

    public void setVerpackungTiefeInCm(String verpackungTiefeInCm) {
        this.verpackungTiefeInCm = verpackungTiefeInCm == null ? null : verpackungTiefeInCm.trim();
    }

    public String getNameTestauszeichnung() {
        return nameTestauszeichnung;
    }

    public void setNameTestauszeichnung(String nameTestauszeichnung) {
        this.nameTestauszeichnung = nameTestauszeichnung == null ? null : nameTestauszeichnung.trim();
    }

    public String getInduktionsfaehigkeit() {
        return induktionsfaehigkeit;
    }

    public void setInduktionsfaehigkeit(String induktionsfaehigkeit) {
        this.induktionsfaehigkeit = induktionsfaehigkeit == null ? null : induktionsfaehigkeit.trim();
    }

    public String getTemperaturbestaendigkeitKorpu() {
        return temperaturbestaendigkeitKorpu;
    }

    public void setTemperaturbestaendigkeitKorpu(String temperaturbestaendigkeitKorpu) {
        this.temperaturbestaendigkeitKorpu = temperaturbestaendigkeitKorpu == null ? null : temperaturbestaendigkeitKorpu.trim();
    }

    public String getTemperaturbestaendigkeitDecke() {
        return temperaturbestaendigkeitDecke;
    }

    public void setTemperaturbestaendigkeitDecke(String temperaturbestaendigkeitDecke) {
        this.temperaturbestaendigkeitDecke = temperaturbestaendigkeitDecke == null ? null : temperaturbestaendigkeitDecke.trim();
    }

    public String getHitzebestaendigBis() {
        return hitzebestaendigBis;
    }

    public void setHitzebestaendigBis(String hitzebestaendigBis) {
        this.hitzebestaendigBis = hitzebestaendigBis == null ? null : hitzebestaendigBis.trim();
    }

    public String getHerstellungshinweis() {
        return herstellungshinweis;
    }

    public void setHerstellungshinweis(String herstellungshinweis) {
        this.herstellungshinweis = herstellungshinweis == null ? null : herstellungshinweis.trim();
    }

    public String getAnzahlTeileDp() {
        return anzahlTeileDp;
    }

    public void setAnzahlTeileDp(String anzahlTeileDp) {
        this.anzahlTeileDp = anzahlTeileDp == null ? null : anzahlTeileDp.trim();
    }

    public String getMasseDurchmesserInCmDp() {
        return masseDurchmesserInCmDp;
    }

    public void setMasseDurchmesserInCmDp(String masseDurchmesserInCmDp) {
        this.masseDurchmesserInCmDp = masseDurchmesserInCmDp == null ? null : masseDurchmesserInCmDp.trim();
    }

    public String getMasseFassungsvermoegenLDp() {
        return masseFassungsvermoegenLDp;
    }

    public void setMasseFassungsvermoegenLDp(String masseFassungsvermoegenLDp) {
        this.masseFassungsvermoegenLDp = masseFassungsvermoegenLDp == null ? null : masseFassungsvermoegenLDp.trim();
    }

    public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1 == null ? null : video1.trim();
    }

    public String getVideo2() {
        return video2;
    }

    public void setVideo2(String video2) {
        this.video2 = video2 == null ? null : video2.trim();
    }

    public String getVideo3() {
        return video3;
    }

    public void setVideo3(String video3) {
        this.video3 = video3 == null ? null : video3.trim();
    }

    public String getVideo4() {
        return video4;
    }

    public void setVideo4(String video4) {
        this.video4 = video4 == null ? null : video4.trim();
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    public String getItemISize() {
        return itemISize;
    }

    public void setItemISize(String itemISize) {
        this.itemISize = itemISize;
    }
}
