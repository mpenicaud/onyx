/**
 * 
 */
package org.obiba.onyx.mica.engine.state;

import org.obiba.onyx.engine.Action;
import org.obiba.onyx.engine.ActionDefinitionBuilder;
import org.obiba.onyx.engine.state.TransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class MicaCompletedState extends AbstractMicaStageState implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(MicaCompletedState.class);

  public void afterPropertiesSet() throws Exception {
    addAction(ActionDefinitionBuilder.CANCEL_ACTION);
  }

  @Override
  public void stop(Action action) {
    super.execute(action);
    log.info("Mica Stage {} is cancelling", super.getStage().getName());
    castEvent(TransitionEvent.CANCEL);
  }

  @Override
  public boolean isCompleted() {
    return true;
  }

  public String getName() {
    return "Mica.Completed";
  }

}