package org.obiba.onyx.quartz.core.wicket.layout;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Page;
import org.obiba.onyx.quartz.core.engine.questionnaire.question.Question;

public class PageQuestionsProvider implements IDataProvider {

  private static final long serialVersionUID = 227294946626164090L;

  private Page page;

  public PageQuestionsProvider(Page page) {
    this.page = page;
  }

  @SuppressWarnings("unchecked")
  public Iterator iterator(int first, int count) {
    return page.getQuestions().iterator();
  }

  public IModel model(Object object) {
    return new Model((Question) object);
  }

  public int size() {
    return page.getQuestions().size();
  }

  public void detach() {
    // TODO Auto-generated method stub

  }

}
