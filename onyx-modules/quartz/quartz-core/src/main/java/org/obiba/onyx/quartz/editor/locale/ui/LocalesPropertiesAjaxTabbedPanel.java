/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.editor.locale.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.util.SetModel;
import org.obiba.onyx.quartz.core.engine.questionnaire.IQuestionnaireElement;
import org.obiba.onyx.quartz.editor.locale.model.LocaleProperties;
import org.obiba.onyx.quartz.editor.locale.predicate.LocalePredicateFactory;

import com.google.common.collect.Iterables;

/**
 * Tabs of Locales and Labels Locales
 * 
 * see https://issues.apache.org/jira/browse/WICKET-2828 when last tabs is deleted
 */
public class LocalesPropertiesAjaxTabbedPanel extends AjaxTabbedPanel {

  private static final long serialVersionUID = 1L;

  private AbstractReadOnlyModel<List<Locale>> dependantModel;

  private IQuestionnaireElement questionnaireElement;

  private SetModel<LocaleProperties> localePropertiesModel;

  /**
   * @param abstractReadOnlyModel
   * @param id
   * @param tabs
   */
  public LocalesPropertiesAjaxTabbedPanel(String id, AbstractReadOnlyModel<List<Locale>> dependantModel, IQuestionnaireElement questionnaireElement, SetModel<LocaleProperties> modelLocaleProperties) {
    super(id, new ArrayList<ITab>());
    this.dependantModel = dependantModel;
    this.questionnaireElement = questionnaireElement;
    this.localePropertiesModel = modelLocaleProperties;
  }

  public LocalesPropertiesAjaxTabbedPanel(String id, IQuestionnaireElement questionnaireElement, SetModel<LocaleProperties> listModelLocaleProperties) {
    this(id, null, questionnaireElement, listModelLocaleProperties);
    initUI();
  }

  private void initUI() {
    for(Iterator<LocaleProperties> iterator = localePropertiesModel.getObject().iterator(); iterator.hasNext();) {
      LocalePropertiesTab localePropertiesTab = new LocalePropertiesTab(iterator.next());
      getTabs().add(localePropertiesTab);
    }
    setSelectedTab(getTabs().size() - 1);
  }

  /**
   * Called to update UI (only for questionnaire)
   */
  public void dependantModelChanged() {
    final List<Locale> listSelectedLocale = dependantModel.getObject();
    int listSelectedLocaleSize = listSelectedLocale.size();
    int listLocalePropertiesSize = localePropertiesModel.getObject().size();

    // remove a tab
    if(listSelectedLocaleSize < listLocalePropertiesSize) {
      Iterable<LocaleProperties> localePropertiesToRemove = Iterables.filter(localePropertiesModel.getObject(), LocalePredicateFactory.newLocalePredicateFilter(listSelectedLocale));
      localePropertiesModel.getObject().removeAll(Arrays.asList(Iterables.toArray(localePropertiesToRemove, LocaleProperties.class)));
      Iterable<ITab> tabToDelete = Iterables.filter(getTabs(), LocalePredicateFactory.newLocalePredicateTabRemover(listSelectedLocale));
      getTabs().removeAll(Arrays.asList(Iterables.toArray(tabToDelete, ITab.class)));

      // not exactly this to do
      if(getTabs().size() != 0) {
        setSelectedTab(getTabs().size() - 1);
      }
    }
    // add a tab
    else if(listSelectedLocaleSize > listLocalePropertiesSize) {
      for(int i = listLocalePropertiesSize; i < listSelectedLocaleSize; i++) {
        final Locale locale = listSelectedLocale.get(i);
        LocaleProperties localeProperties = new LocaleProperties(locale, questionnaireElement);
        LocalePropertiesTab localePropertiesTab = new LocalePropertiesTab(localeProperties);
        localePropertiesModel.getObject().add(localeProperties);
        getTabs().add(localePropertiesTab);
      }
      setSelectedTab(getTabs().size() - 1);
    }
  }
}
