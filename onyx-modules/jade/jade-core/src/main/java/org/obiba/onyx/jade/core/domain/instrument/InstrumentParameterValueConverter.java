package org.obiba.onyx.jade.core.domain.instrument;

import org.obiba.onyx.jade.core.domain.run.InstrumentRunValue;

/**
 * Interface for the InstrumentParameter converters
 * @author acarey
 */

public interface InstrumentParameterValueConverter {

  public void convert(InstrumentRunValue targetInstrumentRunValue, InstrumentRunValue sourceInstrumentRunValue);
  
}
