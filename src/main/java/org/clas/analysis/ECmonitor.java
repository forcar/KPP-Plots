/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clas.analysis;

import java.util.ArrayList;
import org.clas.viewer.AnalysisMonitor;
import org.jlab.clas.physics.Particle;
import org.jlab.detector.base.DetectorType;
import org.jlab.detector.calib.utils.ConstantsManager;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;

/**
 *
 * @author devita
 */
public class ECmonitor extends AnalysisMonitor {
    

    public ECmonitor(String name, ConstantsManager ccdb) {
        super(name,ccdb);
        this.setAnalysisTabNames("Clusters","Pizeros","Electrons");
        this.init(false);
    }

    
    @Override
    public void createHistos() {
        // initialize canvas and create histograms
        this.setNumberOfEvents(0);
        H1F summary = new H1F("summary","summary",6,1,7);
        summary.setTitleX("sector");
        summary.setTitleY("DC hits");
        summary.setFillColor(33);
        DataGroup sum = new DataGroup(1,1);
        sum.addDataSet(summary, 0);
        this.setAnalysisSummary(sum);
        // electrons
        H1F hi_dE_EC = new H1F("hi_dE_EC", "hi_dE_EC", 100, -0.8, 0.8);   
        hi_dE_EC.setTitleX("E-p (GeV)");
        hi_dE_EC.setTitleY("Counts");
        H1F hi_sf_EC = new H1F("hi_sf_EC", "hi_sf_EC", 100,  0.0, 0.6);   
        hi_sf_EC.setTitleX("E/p");
        hi_sf_EC.setTitleY("Counts");
        F1D f1_sf = new F1D("f1_sf","[amp]*gaus(x,[mean],[sigma])", 0.20, 0.30);
        f1_sf.setParameter(0, 0);
        f1_sf.setParameter(1, 0.245);
        f1_sf.setParameter(2, 0.1);
        f1_sf.setLineWidth(3);
        f1_sf.setLineColor(4);
        f1_sf.setOptStat("1111");
        H2F hi_Evsp_EC = new H2F("hi_Evsp_EC", "hi_Evsp_EC", 100, 0, 12, 100, 0, 12);  
        hi_Evsp_EC.setTitleX("p (GeV)"); 
        hi_Evsp_EC.setTitleY("E (GeV)");
        H2F hi_sfvsp_EC = new H2F("hi_sfvsp_EC", "hi_sfvsp_EC", 100, 0, 12, 100, 0, 0.6);  
        hi_sfvsp_EC.setTitleX("p (GeV)"); 
        hi_sfvsp_EC.setTitleY("E/p");       
        F1D sfLow  = new F1D("sfLow",  "[p0]*([p1] + [p2]/0.236/x + [p3]*pow(0.236*x,-2))-5.3*([s0]*([s1]))",1., 10.);
        sfLow.setParameter(0, 0.263);
        sfLow.setParameter(1, 0.985);
        sfLow.setParameter(2,-0.036);
        sfLow.setParameter(3, 0.002);
        sfLow.setParameter(4, 0.0166);
        sfLow.setParameter(5, 1);
        F1D sfHigh = new F1D("sfHigh", "[p0]*([p1] + [p2]/0.236/x + [p3]*pow(0.236*x,-2))+5*([s0]*([s1]))",1., 10.);
        sfHigh.setParameter(0, 0.263);
        sfHigh.setParameter(1, 0.985);
        sfHigh.setParameter(2,-0.036);
        sfHigh.setParameter(3, 0.002);
        sfHigh.setParameter(4, 0.0166);
        sfHigh.setParameter(5, 1);       
        H2F hi_ECin_vs_PCAL = new H2F("hi_ECin_vs_PCAL", "hi_ECin_vs_PCAL", 100, 0, 1, 100, 0, 0.8);  
        hi_ECin_vs_PCAL.setTitleX("PCAL energy (GeV)"); 
        hi_ECin_vs_PCAL.setTitleY("EC energy (GeV)");
//        hi_ECin_vs_PCAL.
        DataGroup dg_electron = new DataGroup(1,5);
        dg_electron.addDataSet(hi_dE_EC, 0);
        dg_electron.addDataSet(hi_Evsp_EC, 1);
        dg_electron.addDataSet(hi_sf_EC, 2);
        dg_electron.addDataSet(f1_sf, 2);
        dg_electron.addDataSet(hi_sfvsp_EC, 3);
        dg_electron.addDataSet(sfLow,  3);
        dg_electron.addDataSet(sfHigh, 3);
        dg_electron.addDataSet(hi_ECin_vs_PCAL, 3);
        this.getDataGroup().add(dg_electron, 1);
        // pizero
        H1F hi_pi0_mass = new H1F("hi_pi0_mass","hi_pi0_mass",200,0.0,400.0);         
        hi_pi0_mass.setTitleX("Pizero Invariant Mass (MeV)");
        hi_pi0_mass.setTitleY("Counts");
        F1D fpi0 = new F1D("fpi0", "[amp]*gaus(x,[mean],[sigma])+[p0]+[p1]*x+[p2]*x*x", 50.,220.);
        fpi0.setParameter(0, 0.0);
        fpi0.setParameter(1, 140.0);
        fpi0.setParameter(2, 2.0);
        fpi0.setParameter(3, 0.0);
        fpi0.setParameter(4, 0.0);
        fpi0.setLineWidth(2);
        fpi0.setLineColor(2);
        fpi0.setOptStat("1111111");
        H1F hi_en_asy = new H1F("hi_en_asy","hi_en_asy",50,-1.0,1.0);      
        hi_en_asy.setTitleX("X:(E1-E2)/(E1+E2)");
        hi_en_asy.setTitleY("Counts");
        H1F hi_angle = new H1F("hi_angle","hi_angle",50, 0., 20.); 
        hi_angle.setTitleX("Two Photon Opening Angle (deg)");
        hi_angle.setTitleY("Counts");
        H2F hi_angle_en = new H2F("hi_angle_en","hi_angle_en",100, 0., 30., 100, 0, 3); 
        hi_angle_en.setTitleX("Two Photon Opening Angle (deg)");
        hi_angle_en.setTitleY("E1*E1 (GeV2)");
        H2F hi_angle_mass = new H2F("hi_angle_mass","hi_angle_mass",50, 0., 20., 100, 10, 250.); 
        hi_angle_mass.setTitleX("Two Photon Opening Angle (deg)");
        hi_angle_mass.setTitleY("Pizero Invariant Mass (MeV)");
        H2F hi_en_mom = new H2F("hi_en_mom","hi_en_mom",100, 0., 5., 100, 0, 5.); 
        hi_en_mom.setTitleX("P (GeV)");
        hi_en_mom.setTitleY("Etot (GeV)");
        DataGroup dg_pion = new DataGroup(1,6);
        dg_pion.addDataSet(hi_pi0_mass, 0);
        dg_pion.addDataSet(fpi0, 0);
        dg_pion.addDataSet(hi_en_asy, 1);
        dg_pion.addDataSet(hi_angle, 2);
        dg_pion.addDataSet(hi_angle_en, 3);
        dg_pion.addDataSet(hi_angle_mass, 4);
        dg_pion.addDataSet(hi_en_mom, 5);
        this.getDataGroup().add(dg_pion, 2);
        //clusters
        H1F hi_etot = new H1F("hi_etot","hi_etot",100, 0., 0.5);
        hi_etot.setTitleX("ECAL tot (GeV)");
        hi_etot.setTitleY("Counts");
        H1F hi_epcal = new H1F("hi_epcal","hi_epcal",100, 0., 0.5);
        hi_epcal.setTitleX("PCAL (GeV)");
        hi_epcal.setTitleY("Counts");
        H1F hi_eecin = new H1F("hi_eecin","hi_eecin",100, 0., 0.5);
        hi_eecin.setTitleX("ECin (GeV)");
        hi_eecin.setTitleY("Counts");
        H1F hi_eecou = new H1F("hi_eecou","hi_eecou",100, 0., 0.5);
        hi_eecou.setTitleX("ECout (GeV)");
        hi_eecou.setTitleY("Counts");
        H1F hi_etot_elec = new H1F("hi_etot_elec","hi_etot_elec",100, 0., 0.5);
        hi_etot_elec.setTitleX("ECAL tot (GeV)");
        hi_etot_elec.setTitleY("Counts");
        hi_etot_elec.setLineColor(4);
        H1F hi_epcal_elec = new H1F("hi_epcal_elec","hi_epcal_elec",100, 0., 0.5);
        hi_epcal_elec.setTitleX("PCAL (GeV)");
        hi_epcal_elec.setTitleY("Counts");
        hi_epcal_elec.setLineColor(4);
        H1F hi_eecin_elec = new H1F("hi_eecin_elec","hi_eecin_elec",100, 0., 0.5);
        hi_eecin_elec.setTitleX("ECin (GeV)");
        hi_eecin_elec.setTitleY("Counts");
        hi_eecin_elec.setLineColor(4);
        H1F hi_eecou_elec = new H1F("hi_eecou_elec","hi_eecou_elec",100, 0., 0.5);
        hi_eecou_elec.setTitleX("ECout (GeV)");
        hi_eecou_elec.setTitleY("Counts");
        hi_eecou_elec.setLineColor(4);
        DataGroup dg_cluster = new DataGroup(2,2);
        dg_cluster.addDataSet(hi_etot, 0);
        dg_cluster.addDataSet(hi_epcal, 1);
        dg_cluster.addDataSet(hi_eecin, 2);
        dg_cluster.addDataSet(hi_eecou, 3);
        dg_cluster.addDataSet(hi_etot_elec, 0);
        dg_cluster.addDataSet(hi_epcal_elec, 1);
        dg_cluster.addDataSet(hi_eecin_elec, 2);
        dg_cluster.addDataSet(hi_eecou_elec, 3);        
        this.getDataGroup().add(dg_cluster, 3);        
    }
    
    @Override
    public void plotHistos() {
        this.getAnalysisCanvas().getCanvas("Electrons").divide(2, 2);
        this.getAnalysisCanvas().getCanvas("Electrons").setGridX(false);
        this.getAnalysisCanvas().getCanvas("Electrons").setGridY(false);
        this.getAnalysisCanvas().getCanvas("Pizeros").divide(2, 2);
        this.getAnalysisCanvas().getCanvas("Pizeros").setGridX(false);
        this.getAnalysisCanvas().getCanvas("Pizeros").setGridY(false);
        this.getAnalysisCanvas().getCanvas("Clusters").divide(2, 2);
        this.getAnalysisCanvas().getCanvas("Clusters").setGridX(false);
        this.getAnalysisCanvas().getCanvas("Clusters").setGridY(false);
        
        this.getAnalysisCanvas().getCanvas("Electrons").cd(0);
//        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getH1F("hi_dE_EC"));
        this.getAnalysisCanvas().getCanvas("Electrons").getPad(0).getAxisZ().setLog(true);
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getH2F("hi_ECin_vs_PCAL"));
        this.getAnalysisCanvas().getCanvas("Electrons").cd(1);
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getH1F("hi_sf_EC"));
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getF1D("f1_sf"),"same");
        this.getAnalysisCanvas().getCanvas("Electrons").cd(2);
        this.getAnalysisCanvas().getCanvas("Electrons").getPad(2).getAxisZ().setLog(true);
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getH2F("hi_Evsp_EC"));
        this.getAnalysisCanvas().getCanvas("Electrons").cd(3);
        this.getAnalysisCanvas().getCanvas("Electrons").getPad(3).getAxisZ().setLog(true);
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getH2F("hi_sfvsp_EC"));
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getF1D("sfLow"),"same");
        this.getAnalysisCanvas().getCanvas("Electrons").draw(this.getDataGroup().getItem(1).getF1D("sfHigh"),"same");
        
        this.getAnalysisCanvas().getCanvas("Pizeros").cd(0);
        this.getAnalysisCanvas().getCanvas("Pizeros").draw(this.getDataGroup().getItem(2).getH1F("hi_pi0_mass"));
        this.getAnalysisCanvas().getCanvas("Pizeros").draw(this.getDataGroup().getItem(2).getF1D("fpi0"),"same");
        this.getAnalysisCanvas().getCanvas("Pizeros").cd(1);
        this.getAnalysisCanvas().getCanvas("Pizeros").draw(this.getDataGroup().getItem(2).getH1F("hi_en_asy"));
        this.getAnalysisCanvas().getCanvas("Pizeros").cd(2);
        this.getAnalysisCanvas().getCanvas("Pizeros").draw(this.getDataGroup().getItem(2).getH1F("hi_angle"));
        this.getAnalysisCanvas().getCanvas("Pizeros").cd(3);
        this.getAnalysisCanvas().getCanvas("Pizeros").getPad(3).getAxisZ().setLog(true);
        this.getAnalysisCanvas().getCanvas("Pizeros").draw(this.getDataGroup().getItem(2).getH2F("hi_angle_en"));
        
        this.getAnalysisCanvas().getCanvas("Clusters").cd(0);
        this.getAnalysisCanvas().getCanvas("Clusters").getPad(0).getAxisY().setLog(true);
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_etot"));
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_etot_elec"),"same");
        this.getAnalysisCanvas().getCanvas("Clusters").cd(1);
        this.getAnalysisCanvas().getCanvas("Clusters").getPad(1).getAxisY().setLog(true);
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_epcal"));
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_epcal_elec"),"same");
        this.getAnalysisCanvas().getCanvas("Clusters").cd(2);
        this.getAnalysisCanvas().getCanvas("Clusters").getPad(2).getAxisY().setLog(true);
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_eecin"));
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_eecin_elec"),"same");
        this.getAnalysisCanvas().getCanvas("Clusters").cd(3);
        this.getAnalysisCanvas().getCanvas("Clusters").getPad(3).getAxisY().setLog(true);
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_eecou"));
        this.getAnalysisCanvas().getCanvas("Clusters").draw(this.getDataGroup().getItem(3).getH1F("hi_eecou_elec"),"same");
        
        this.getAnalysisCanvas().getCanvas("Electrons").update();
        this.getAnalysisCanvas().getCanvas("Pizeros").update();    
        this.getAnalysisCanvas().getCanvas("Clusters").update();    
    }
        
    @Override
    public void processEvent(DataEvent event) {
        // process event info and save into data group        
        ArrayList<Particle> partGamma = new ArrayList();
        // EC clusters
        if(event.hasBank("ECAL::clusters")==true){
            DataBank  bank = event.getBank("ECAL::clusters");
            int rows = bank.rows();
            double energy1=0;
            double energy4=0;
            double energy7=0;
            for(int loop = 0; loop < rows; loop++){
                int layer    = bank.getByte("layer", loop);
                float energy = bank.getFloat("energy",loop);
                if(layer==1) {
                    energy1=energy;
                    this.getDataGroup().getItem(3).getH1F("hi_epcal").fill(energy);
                }
                else if (layer==4) {
                    energy4=energy;
                    this.getDataGroup().getItem(3).getH1F("hi_eecin").fill(energy);
                }
                else if(layer==7) {
                    energy7=energy;
                    this.getDataGroup().getItem(3).getH1F("hi_eecou").fill(energy);
                }
            }
            if(energy1+energy4+energy7>0) this.getDataGroup().getItem(3).getH1F("hi_etot").fill(energy1+energy4+energy7);
        }
        // event builder
        DataBank recBankEB = null;
        DataBank recDeteEB = null;
        DataBank recBankTB = null;
        if(event.hasBank("REC::Particle")) recBankEB = event.getBank("REC::Particle");
        if(event.hasBank("REC::Calorimeter")) recDeteEB = event.getBank("REC::Calorimeter");
        if(event.hasBank("TimeBasedTrkg::TBTracks")) recBankTB = event.getBank("TimeBasedTrkg::TBTracks");
        if(recBankEB!=null && recDeteEB!=null) {
            int nrows = recBankEB.rows();
            for(int loop = 0; loop < nrows; loop++){
                    int pidCode = 0;
                    if(recBankEB.getInt("pid", loop)!=0) pidCode = recBankEB.getInt("pid", loop);
                    else if(recBankEB.getByte("charge", loop)==-1) pidCode = -211;
                    else if(recBankEB.getByte("charge", loop)==1) pidCode = 211;
                    else pidCode = 22;
                    Particle recParticle = new Particle(
                                                pidCode,
                                                recBankEB.getFloat("px", loop),
                                                recBankEB.getFloat("py", loop),
                                                recBankEB.getFloat("pz", loop),
                                                recBankEB.getFloat("vx", loop),
                                                recBankEB.getFloat("vy", loop),
                                                recBankEB.getFloat("vz", loop));
//                   if(recBankEB.getByte("status", loop)==1) {
                    double energy1=0;
                    double energy4=0;
                    double energy7=0;
                    int    sector =0;
                    int  detector =0;
                    for(int j=0; j<recDeteEB.rows(); j++) {
                        if(recDeteEB.getShort("pindex",j)==loop && recDeteEB.getByte("detector",j)==DetectorType.ECAL.getDetectorId()) {
                            detector = recDeteEB.getByte("detector",j);
                            if(energy1 >= 0 && recDeteEB.getByte("layer",j) == 1) {
                                energy1 += recDeteEB.getFloat("energy",j);
                                sector = recDeteEB.getByte("sector",j);
                            }
                            if(energy4 >= 0 && recDeteEB.getByte("layer",j) == 4) energy4 += recDeteEB.getFloat("energy",j);
                            if(energy7 >= 0 && recDeteEB.getByte("layer",j) == 7) energy7 += recDeteEB.getFloat("energy",j);
                        }
                    }
                    recParticle.setProperty("energy1",energy1);
                    recParticle.setProperty("energy4",energy4);
                    recParticle.setProperty("energy7",energy7);
                    recParticle.setProperty("sector",sector*1.0);
                    recParticle.setProperty("detector",detector*1.0);
                    if(recParticle.charge()==0 && recParticle.getProperty("detector")==DetectorType.ECAL.getDetectorId()) {
                        if(energy1>0.05 && energy4>0.0) {
                            partGamma.add(recParticle);
//                            System.out.println(recBankEB.getShort("status",loop));
                            double energy=(energy1+energy4+energy7)/0.245;
                            this.getDataGroup().getItem(2).getH2F("hi_en_mom").fill(recParticle.p(),energy);
                        }                            
                    }
                    if(recParticle.charge()==-1 && energy1>0.0 && energy4>0) {
                        double energy=(energy1+energy4+energy7)/0.245;
                        this.getDataGroup().getItem(1).getH2F("hi_ECin_vs_PCAL").fill(energy1,energy4+energy7);
                        if(energy1>0.1 && energy4>0.0 && recParticle.p()>0) {
                            this.getDataGroup().getItem(1).getH2F("hi_Evsp_EC").fill(recParticle.p(),energy);
                            this.getDataGroup().getItem(1).getH2F("hi_sfvsp_EC").fill(recParticle.p(),(energy1+energy4+energy7)/recParticle.p());
                            this.getDataGroup().getItem(1).getH1F("hi_dE_EC").fill(energy-recParticle.p());
                            this.getDataGroup().getItem(1).getH1F("hi_sf_EC").fill((energy1+energy4+energy7)/recParticle.p());
//                            System.out.println(energy1 + " " + energy4 + " " + energy7);
//                            recDeteEB.show();
                        }
                        this.getDataGroup().getItem(3).getH1F("hi_etot_elec").fill(energy1+energy4+energy7);
                        this.getDataGroup().getItem(3).getH1F("hi_epcal_elec").fill(energy1);
                        this.getDataGroup().getItem(3).getH1F("hi_eecin_elec").fill(energy4);
                        this.getDataGroup().getItem(3).getH1F("hi_eecou_elec").fill(energy7);
                    }
                }
                if(partGamma.size()>=2) {
                    for(int i1=0; i1<partGamma.size(); i1++) {
                        for(int i2=i1+1; i2<partGamma.size(); i2++) {
                            Particle partGamma1 = partGamma.get(i1);
                            Particle partGamma2 = partGamma.get(i2);
        //                    if(partGamma1.p()>0.05 && partGamma2.p()>0.05) {
                            Particle partPi0 = new Particle();
                            partPi0.copy(partGamma1);
                            partPi0.combine(partGamma2, +1);
                            if(partGamma1.p()*partGamma2.p()==1) recBankEB.show();
                            double   invmass = 1e3*Math.sqrt(partPi0.mass2());
                            double   x       = (partGamma1.p()-partGamma2.p())/(partGamma1.p()+partGamma2.p());
                            double   angle   = Math.toDegrees(Math.acos(partGamma1.cosTheta(partGamma2)));
                            if(angle>3 && angle<17) {
//                                System.out.println(partGamma.size() + " " + i1 + " " + partGamma1.p() + " " + i2 + " " + partGamma2.p() + " " + invmass);
                                this.getDataGroup().getItem(2).getH1F("hi_pi0_mass").fill(invmass);  //Two-photon invariant mass
                                this.getDataGroup().getItem(2).getH1F("hi_en_asy").fill(x);
                                this.getDataGroup().getItem(2).getH2F("hi_angle_mass").fill(angle,invmass);
                            }
                            this.getDataGroup().getItem(2).getH1F("hi_angle").fill(angle);
                            this.getDataGroup().getItem(2).getH2F("hi_angle_en").fill(angle,Math.sqrt(partGamma1.p()*partGamma2.p()));
                        }
                    }
                }
        }
   }

    @Override
    public void timerUpdate() {
//        System.out.println("Updating DC");
        //fitting negative tracks vertex
        H1F hi_sf = this.getDataGroup().getItem(1).getH1F("hi_sf_EC");
        this.getDataGroup().getItem(1).getF1D("f1_sf").setParameter(0, hi_sf.getBinContent(hi_sf.getMaximumBin()));
        this.getDataGroup().getItem(1).getF1D("f1_sf").setParameter(1, hi_sf.getDataX(hi_sf.getMaximumBin()));
        DataFitter.fit(this.getDataGroup().getItem(1).getF1D("f1_sf"), hi_sf, "Q"); //No options uses error for sigma
        // fit pi0 mass
        H1F h1p = this.getDataGroup().getItem(2).getH1F("hi_pi0_mass");
        F1D f1p = this.getDataGroup().getItem(2).getF1D("fpi0");
        double hAmp  = h1p.getBinContent(h1p.getMaximumBin());
        double hMean = h1p.getAxis().getBinCenter(h1p.getMaximumBin());
        double hRMS  = 10; //ns
        f1p.setParameter(0, hAmp);
        f1p.setParLimits(0, hAmp*0.8, hAmp*1.2);
        f1p.setParameter(1, hMean);
        f1p.setParLimits(1, hMean-hRMS, hMean+hRMS);
        DataFitter.fit(f1p,h1p,"LQ");
        h1p.setFunction(null);        
    }
}
