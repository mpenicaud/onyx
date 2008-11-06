/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.ruby.engine.state;

import org.apache.wicket.Component;
import org.obiba.onyx.engine.Action;
import org.obiba.onyx.engine.ActionDefinitionBuilder;
import org.obiba.onyx.engine.ActionType;
import org.obiba.onyx.engine.state.TransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Ruby IN PROGRESS state.
 */
public class RubyInProgressState extends AbstractRubyStageState implements InitializingBean {
  //
  // Constants
  //

  private static final Logger log = LoggerFactory.getLogger(RubyInProgressState.class);

  //
  // InitializingBean Methods
  //

  public void afterPropertiesSet() throws Exception {
    addAction(ActionDefinitionBuilder.create(ActionType.STOP, "Cancel").setDescription("You may explain why you are cancelling this stage.").getActionDefinition());
    addAction(ActionDefinitionBuilder.create(ActionType.INTERRUPT, "Interrupt").getActionDefinition());
    addSystemAction(ActionDefinitionBuilder.COMPLETE_ACTION);
  }

  //
  // AbstractRubyStageState Methods
  //

  public String getName() {
    return "Ruby.InProgress";
  }

  public Component getWidget(String id) {
    return null;
  }

  @Override
  public void stop(Action action) {
    log.info("Ruby Stage {} is stopping", super.getStage().getName());

    if(areDependenciesCompleted() != null && areDependenciesCompleted()) {
      castEvent(TransitionEvent.CANCEL);
    } else {
      castEvent(TransitionEvent.INVALID);
    }
  }

  @Override
  public void complete(Action action) {
    log.info("Ruby Stage {} is completing", super.getStage().getName());
    castEvent(TransitionEvent.COMPLETE);
  }

  @Override
  public boolean isInteractive() {
    return false;
  }

  @Override
  public void interrupt(Action action) {
    log.info("Ruby Stage {} is interrupting", super.getStage().getName());
    castEvent(TransitionEvent.INTERRUPT);
  }
}
