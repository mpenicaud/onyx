/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.core.wicket.wizard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentOutputParameter;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentParameterCaptureMethod;
import org.obiba.onyx.jade.core.domain.instrument.InstrumentType;
import org.obiba.onyx.jade.core.domain.instrument.validation.IntegrityCheck;
import org.obiba.onyx.jade.core.domain.run.InstrumentRun;
import org.obiba.onyx.jade.core.domain.run.InstrumentRunStatus;
import org.obiba.onyx.jade.core.domain.run.InstrumentRunValue;
import org.obiba.onyx.jade.core.service.ActiveInstrumentRunService;
import org.obiba.onyx.jade.core.wicket.instrument.InstrumentLaunchPanel;
import org.obiba.onyx.util.data.Data;
import org.obiba.onyx.wicket.wizard.WizardForm;
import org.obiba.onyx.wicket.wizard.WizardStepPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstrumentLaunchStep extends WizardStepPanel {

  private static final long serialVersionUID = -2511672064460152210L;

  private static final Logger log = LoggerFactory.getLogger(InstrumentLaunchStep.class);

  @SpringBean
  private ActiveInstrumentRunService activeInstrumentRunService;

  private boolean launched = false;

  public InstrumentLaunchStep(String id) {
    super(id);
    setOutputMarkupId(true);

    add(new Label("title", new StringResourceModel("InstrumentApplicationLaunch", this, null)));
  }

  @Override
  public void handleWizardState(WizardForm form, AjaxRequestTarget target) {
    form.getPreviousLink().setEnabled(getPreviousStep() != null);
    form.getNextLink().setEnabled(true);
    form.getFinishLink().setEnabled(false);
    if(target != null) {
      target.addComponent(form.getPreviousLink());
      target.addComponent(form.getNextLink());
    }
  }

  @SuppressWarnings("serial")
  @Override
  public void onStepInNext(final WizardForm form, AjaxRequestTarget target) {
    super.onStepInNext(form, target);
    setContent(target, new InstrumentLaunchPanel(getContentId()) {

      @Override
      public void onInstrumentLaunch() {
        log.info("onInstrumentLaunch");
        activeInstrumentRunService.setInstrumentRunStatus(InstrumentRunStatus.IN_PROGRESS);
        launched = true;
      }

    });
  }

  @Override
  public void onStepOutNext(WizardForm form, AjaxRequestTarget target) {
    super.onStepOutNext(form, target);

    if(launched || instrumentRunContainsValues()) {
      if(InstrumentRunStatus.IN_ERROR.equals(activeInstrumentRunService.getInstrumentRun().getStatus())) {
        error(getString("InstrumentApplicationError"));
        setNextStep(null);
      } else {
        InstrumentType instrumentType = activeInstrumentRunService.getInstrumentType();

        List<InstrumentOutputParameter> outputParams = instrumentType.getOutputParameters(InstrumentParameterCaptureMethod.AUTOMATIC);

        boolean completed = true;

        if(!instrumentType.isRepeatable()) {
          for(InstrumentOutputParameter param : outputParams) {
            InstrumentRunValue runValue = activeInstrumentRunService.getInstrumentRunValue(param.getCode());
            if(runValue == null) {
              completed = false;
            } else {
              Data data = runValue.getData(param.getDataType());
              if(data == null || data.getValue() == null) {
                completed = false;
              }
            }
            if(!completed) {
              log.warn("Missing value for the following output parameter: {}", param.getVendorName());
              error(getString("NoInstrumentDataSaveThem"));
              setNextStep(null);
              break;
            }
          }
        } else {

          int currentCount = activeInstrumentRunService.getInstrumentRun().getMeasureCount();
          if(!((InstrumentLaunchPanel) get(getContentId())).getSkipMeasurement() || currentCount == 0) {
            // minimum is having the expected count of repeatable measures
            int expectedCount = instrumentType.getExpectedMeasureCount(activeInstrumentRunService.getParticipant());
            if(currentCount < expectedCount) {
              completed = false;
              error(getString("MissingMeasure", new Model(new ValueMap("count=" + (expectedCount - currentCount)))));
              setNextStep(null);
            }
          }
        }

        if(completed) {
          // Perform each output parameter's integrity checks.
          List<IntegrityCheck> failedChecks = activeInstrumentRunService.checkIntegrity(outputParams);

          if(failedChecks.isEmpty()) {
            ((InstrumentWizardForm) form).setUpWizardFlow();
          } else {
            setNextStep(null);
          }
        }
      }

    } else {
      error(getString("InstrumentApplicationMustBeStarted"));
      setNextStep(null);
    }
  }

  private boolean instrumentRunContainsValues() {
    InstrumentRun run = activeInstrumentRunService.getInstrumentRun();
    if(run.getInstrumentRunValues() != null && run.getInstrumentRunValues().size() > 0) return true;
    if(run.getMeasureCount() > 0) return true;
    return false;
  }
}
