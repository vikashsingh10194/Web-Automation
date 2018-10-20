package com.via.pageobjects.flights.domestic;

import org.apache.commons.lang3.StringUtils;

import com.via.utils.Constant;

/***
 * this enum used to check whether airline provide service in sector or not.
 *
 */
public enum Airlines {
  AIRASIA_INDIA {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && (StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXC)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.COK)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.PNQ) || StringUtils
            .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.DEL) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXC) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GOI) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU) || StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.JAI) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.PNQ) || StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.COK) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR) || StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.PNQ) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR) || StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)))

          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.VTZ) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.COK) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL)))) {

        return true;
      }

      return false;
    }
  },

  AIR_COSTA {

    public boolean hasFlight(String source, String destination) {

      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.AMD) && (StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
          || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI) || StringUtils
            .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.TIR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CJB) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.TIR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.TIR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.JAI) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.TIR) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.VGA) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.TIR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VTZ)))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && (StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.AMD)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.CJB)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.TIR)
              || StringUtils.equalsIgnoreCase(destination, Constant.CityConstantsFlights.VGA) || StringUtils
                .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD)))) {

        return true;

      }

      return false;

    }

  },

  AIR_PEGASUS {
    public boolean hasFlight(String source, String destination) {

      if (((source.equalsIgnoreCase(Constant.CityConstantsFlights.IXM)
          || source.equalsIgnoreCase(Constant.CityConstantsFlights.MAA) || source
            .equalsIgnoreCase(Constant.CityConstantsFlights.TRV))
          || source.equalsIgnoreCase(Constant.CityConstantsFlights.BLR) || source
            .equalsIgnoreCase(Constant.CityConstantsFlights.IXE))
          && (destination.equalsIgnoreCase(Constant.CityConstantsFlights.IXM)
              || destination.equalsIgnoreCase(Constant.CityConstantsFlights.MAA)
              || destination.equalsIgnoreCase(Constant.CityConstantsFlights.BLR)
              || destination.equalsIgnoreCase(Constant.CityConstantsFlights.IXE) || destination

          .equalsIgnoreCase(Constant.CityConstantsFlights.TRV))) {

        return true;

      }

      return false;

    }

  },
  AIRINDIA {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))) {
        return false;
      }
      return true;
    }
  },
  GOAIR {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.SXR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXB))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXB) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.SXR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.SXR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.SXR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RPR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RPR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXB))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXB) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GOI) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.VNS) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.DEL) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.VNS))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HBX))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HBX) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.COK) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.TRV))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.TRV) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.COK))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RJA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RJA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))) {

        return false;
      }

      return true;
    }
  },
  INDIGO {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HBX))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HBX) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.SXR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.SXR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))) {

        return false;
      }

      return true;
    }
  },
  JETAIRWAYS {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HBX))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))) {

        return false;

      }
      return true;
    }
  },
  SPICEJET {
    public boolean hasFlight(String source, String destination) {

      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.SXR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.SXR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RPR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RPR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GOI) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HBX))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HBX) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))) {
        return false;
      }

      return true;
    }
  },
  TRUEJET {
    public boolean hasFlight(String source, String destination) {
      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXC))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXC) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RJA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RJA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GOI) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))) {
        return true;
      }
      return false;
    }
  },
  VISTARA {
    public boolean hasFlight(String source, String destination) {

      if ((StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.SXR) && StringUtils
          .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.SXR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RPR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RPR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXB))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXB) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GOI))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GOI) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.JAI))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.JAI) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXC))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXC) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.DEL))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.DEL) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HBX))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HBX) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.COK) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.TRV))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.TRV) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.COK))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.RJA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.RJA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.HYD) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.HYD))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.MAA) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.BLR))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.BLR) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.MAA))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.GAU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IMF))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IMF) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.GAU))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.CCU) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.IXZ))
          || (StringUtils.equalsIgnoreCase(source, Constant.CityConstantsFlights.IXZ) && StringUtils
              .equalsIgnoreCase(destination, Constant.CityConstantsFlights.CCU))) {

        return false;
      }

      return true;
    }
  };
  public abstract boolean hasFlight(String source, String destination);
}
