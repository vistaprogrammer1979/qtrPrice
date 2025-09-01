package com.santechture.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

    public enum CodeType {
        CPT(3),
        HCPCS(4),
        TRADE_DRUG(5),
        DENTAL(6),
        SERVICE(8),
        IR_DRG(9),
        GENERIC_DRUG(10),
        ICD9(17),
        ICD10(16),
        Package(18),
        CustomCode(19) ,
        PackagedServices(30),
        LabSupplies(31),
        Others(32),
        StaffHealthInsuranceCharge(33),
        Clinical(34),
        General(35),
        RoomAndBed(36),
        INVESTIGATION(37),
        Other(38),
        PharmaceticalsAndSupplies(39),
        onc(40),
//        LabAbroadCharges(41),
        Pharmacy(730),
//        Transportation(42),

        // Qatar
        OP_QOCS(41),
        ER_URG(42),
        DENTAL_ASDSG(43),
        RADIOLOGY_ACHI(44),
        IP_AR_DRG(45),
        Medicine(46),

//        KSA
        AccomdationCharges(10000),
        EEG(100000),
        Embalming(110000),
        EndocrinologtDM(120000),
        Endoscopy(130000),
        ENT(140000),
        RoomBoard(150000),
        Gynaecologicalprocedures(160000),
        PackageDeal(170000),
        ICUNICU(180000),
        Imagingservices(190000),
        ARTCareUnit(20000),
        InternalMedicine(200000),
        JobServices(210000),
        KSAServiceCodes(220000),
        Laboratory(230000),
        LaboratoryandPathologyServices(240000),
        MedicalRehabilitaion(250000),
        NephrologyRenalDialysis(260000),
        NeuroSurgery(270000),
        Neurophysiology(280000),
        Noninvasivecognitiveandotherinterventionsnotelsewhereclassified(290000),
        CardiacSciences(30000),
        ObstericsGynacology(300000),
        Obstetricprocedures(310000),
        Ophthalomology(320000),
        ORTHOPEDICSURGERY(330000),
        OutpatientEmergencyServices(340000),
        PHCCCons(350000),
        pediatricsurgery(360000),
        CARDICSYRGERY(370000),
        PhysicalTherapy(380000),
        plasterroom(390000),
        Pharmacy_(40000),
        PlasticSurgery(400000),
        Proceduresonbloodandbloodformingorgans(410000),
        Proceduresonbreast(420000),
        Proceduresoncardiovascularsystem(430000),
        Proceduresondigestivesystem(440000),
        Proceduresonearandmastoidprocess(450000),
        Proceduresonendocrinesystem(460000),
        Proceduresoneyeandadnexa(470000),
        Proceduresonmalegenitalorgans(480000),
        Proceduresonmusculoskeletalsystem(490000),
        CHEMOTHERAPY(50000),
        Proceduresonnervoussystem(500000),
        Proceduresonnosemouthandpharynx(510000),
        Proceduresonrespiratorysystem(520000),
        Proceduresonurinarysystem(530000),
        PSYCHIATRIC(540000),
        PulmonaryRespiratory(550000),
        Radiationoncologyprocedures(560000),
        Radiology(570000),
        SupportServices(580000),
        ToxicologyandForensicChemistry(590000),
        Dental(60000),
//        Transportation(60),
        VACCINATION(610000),
        VascluerSurgery(620000),
        GeneralSurgery(630000),
        HomeVisit(640000),
        Dermatologicalandplasticprocedures(70000),
        Dermatology(80000),
        DietaryDepartment(90000),
        DRGservices(650000),
        InPatientServices(660000),
        //New Activity Group for Nphies Integeration
        Procedures_on_nervous_system(10001),
        Procedures_on_endocrine_system(10002),
        Procedures_on_eye_and_adnexa(10003),
        Procedures_on_ear_and_mastoid_process(10004),
        Procedures_on_nose_mouth_and_pharynx(10005),
        Procedures_on_respiratory_system(10006),
        Procedures_on_cardiovascular_system(10007),
        Procedures_on_blood_and_blood_forming_organs(10008),
        Procedures_on_digestive_system(10009),
        Procedures_on_urinary_system(10010),
        Procedures_on_male_genital_organs(10011),
        Gynaecological_procedures(10012),
        Obstetric_procedures(10013),
        Procedures_on_musculoskeletal_system(10014),
        Dermatological_and_plastic_procedures(10015),
        Procedures_on_breast(10016),
        //Radiation_oncology_procedures(10017),
        Non_invasive_cognitive_and_other_interventions_not_elsewhere_classified(10018),
        Imaging_services(10019),
        EMS(10020),
        Laboratory_and_Pathology(10021),
        Ambulance_and_Transport_Services(10022),
        KSA_Service_Codes(10023),
        Mortuary_Services(10024),


//        CPT(300),
//        HCPCS(400),
//        TRADE_DRUG(500),
//        IR_DRG(900),
//        GENERIC_DRUG(1000),
//        DENTAL(800),
//        ICD9(1700),
//        ICD10(1800),
        LABORATORY(101),
        RADIOLOGY(102),
        CONSULTATION(103),
        MEDICATION_(104),
//        SERVICE(105),
        CONSUMABLES(106),
        AliTest(99999),
        Obstetric_Procedures(1330),
        Radiation_oncology_procedures(1700),
        Immunisation(1800),
        Mental_Health(1823),
            DENTAL_IMAGING(1770),
            AMBULANCE_SERVICES(1711),
            ANAESTHESIOLOGY(1712),
            CARDIOLOGY(1713),
            CARDIOVASCULAR_SURGERY(1714),
            CONSULTATIONS(3600),
            DERMATOLOGY(1716),
            DIETITIAN_NUTRITION_SERVICES(1717),
            GASTROENTEROLOGY(1718),
            GENERAL_SERVICES(1719),
            GENERAL_SURGERY(1720),
            HAEMODIALYSIS_PERITONEAL(1721),
            HEMATOLOGY_ONCOLOGY(1722),
            IMMUNIZATION_SERVICES(1723),
            INFUSION_SERVICES(1724),
            INTERVENTIONAL_RADIOLOGY(1725),
            INVASIVE_CARDIOLOGY(1726),
            LABORATORY_SERVICES(1728),
            MENTAL_HEALTH(1729),
            NEUROLOGY(1730),
            NEUROSURGERY(1731),
            NURSING_SERVICES(1732),
            OBGYN(1733),
            OPHTHALMOLOGY(1734),
            OPTOMETRY_SERVICES(1735),
            ORAL_MAXILLOFACIAL_SURGERY(1736),
            ORTHOPEDICS(1737),
            ORTHOTIC_SERVICES(1738),
            OTOLARYNGOLOGY(1739),
            PHYSIOLOGICAL_SERVICES(1740),
            PLASTIC_SURGERY(1741),
            PULMONOLOGY(1742),
            RADIATION_ONCOLOGY(1743),
            RADIOLOGY_SERVICES(1744),
            REHABILITATION_SERVICES(1745),
            RESPIRATORY_SERVICES(1746),
            ROOM_AND_BOARD(1747),
            SPINAL_SURGERY(1748),
            THORACIC_SURGERY(1749),
            UROLOGY(1750),
            VASCULAR_LAB(1751),
            MEDICAL_SUPPLIES(5522),
            MEDICAL_SUPPLIES_AD_HOC(1769),
            PACKAGES(1754),
            DENTAL_SERVICES(1755),
            HOME_HEALTH_CARE_SERVICES(1756),
            Package_Deal(170000),
            OTHER_SERVICE(107),
            GLOBEMED_MOH(100),

            Per_Diem_Packages(2200),
            compoundmedication(111)
    /*Per_Diem_Packages(2200),
    DRG_Based_Service(2205),
    JHAH_Medical_Check_up(2211),
    Pandemic_Vaccine(2215),
    JHAH_RESEARCH(2225),
    Infusions_admin(2233),
    JHAH_SPS_Services(2241),
    JHAH_CPPL_Services(2242),
    JHAH_Non_Contracted(2243),
    JHAH_Aramco_Services(2244),
    JHAH_Per_Capita(2245),
    JHAH_Outreach_services(2246),
    Promotional_Service(2255),
    JHAH_CONTRACTED_SERVICE(2299),
    Procedures(3300),
    Ambulance_services(3500),
    Consultations(3600),
    Room_and_Board(3610),
    Homecare(3620),
    Occupational_Medicine(3622),
    Haemodialysis_Peritoneal(3630),
    OR_ROOM_and_CATH_LAB(3670),
    Emergency_Service(3680),
    Dental_Inpatient_Services(4500),
    Radiology_(5500),
    Medcial_Supplies(5522),
    Physcial_Occupational_Therapy(5533),
    Pharmacy(6730),
    Laboratory_(7300),
    Laboratory_SEND_OUT(7322),
    Dental_OPD_ADA_Services(9700),
    CHI_Custom_code(9900),
    MISCELLANEOUS_UNLISTED_CODE(9999),
    Anaesthesiology(6333),
    Package_Deal(17),
    OTHER_SERVICE(107),
    compoundmedication(111)*/;





        private final int val;
        CodeType(int val)
        {
            this.val = val;
        }
        @JsonValue
        public int getValue() { return val; }


        private static final Map<Integer, CodeType> _map = new HashMap<Integer, CodeType>();
        static
        {
            for (CodeType codeType : CodeType.values())
                _map.put(codeType.val, codeType);
        }
        @JsonCreator
        public static CodeType from(int value)
        {
            return _map.get(value);
        }
        private boolean test(){
            return CodeType.HCPCS.getValue() == 4;
        }

        public static CodeType fromKSACodeType(com.santechture.request.ksa.CodeType codeType){
                if (codeType == null){
                        System.out.println("codeType is null");
                        return null;
                }
                var ct =  switch (codeType){
                        case Pharmacy -> CodeType.Pharmacy;
                        case CPT -> CodeType.CPT;
                        case HCPCS -> CodeType.HCPCS;
                        case TRADE_DRUG -> CodeType.TRADE_DRUG;
                        case IR_DRG -> CodeType.IR_DRG;
                        case GENERIC_DRUG -> CodeType.GENERIC_DRUG;
                        case DENTAL -> CodeType.DENTAL;
                        case ICD9 -> CodeType.ICD9;
                        case ICD10 -> CodeType.ICD10;
                        case SERVICE -> CodeType.SERVICE;
                        case EEG -> CodeType.EEG;
                        default -> CodeType.valueOf(codeType.name());
                };
//                System.out.println("replace codeType "+codeType +" with codeType " + ct);
                return ct;
        }

    }
