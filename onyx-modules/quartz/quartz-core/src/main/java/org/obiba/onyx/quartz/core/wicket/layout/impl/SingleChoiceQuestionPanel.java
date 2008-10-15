package org.obiba.onyx.quartz.core.wicket.layout.impl;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Question;
import org.obiba.onyx.quartz.core.wicket.layout.QuestionPanel;
import org.obiba.onyx.quartz.core.wicket.model.QuestionnaireStringResourceModel;

public class SingleChoiceQuestionPanel extends QuestionPanel {

  private static final long serialVersionUID = 2951128797454847260L;

  public SingleChoiceQuestionPanel(String id, IModel model) {
    super(id, model);
    
    Question question = (Question)model.getObject();
    add(new Label("label", new QuestionnaireStringResourceModel(question, "label", null)));
  }
  
  public void onNext() {
    // TODO Auto-generated method stub

  }

  public void onPrevious() {
    // TODO Auto-generated method stub

  }

}
