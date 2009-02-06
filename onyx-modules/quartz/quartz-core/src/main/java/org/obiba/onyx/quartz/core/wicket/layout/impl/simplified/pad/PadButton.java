/*******************************************************************************
 * Copyright 2008(c) The OBiBa Consortium. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.quartz.core.wicket.layout.impl.simplified.pad;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * 
 */
public class PadButton extends Panel {

  private static final long serialVersionUID = 1L;

  /**
   * @param id
   */
  @SuppressWarnings("serial")
  public PadButton(String id, IModel model) {
    super(id, model);
    AjaxLink link = new AjaxLink("button") {

      @Override
      public void onClick(AjaxRequestTarget target) {
        IPadSelectionListener listener = (IPadSelectionListener) PadButton.this.findParent(IPadSelectionListener.class);
        if(listener != null) {
          listener.onPadSelection(target, PadButton.this.getModel());
        }
      }

    };
    link.add(new AttributeModifier("value", model));
    add(link);

  }

  /**
   * Enable or disable the inner component representing the button.
   * @param enabled
   * @return this for chaining
   */
  public PadButton setButtonEnabled(boolean enabled) {
    get("button").setEnabled(enabled);
    return this;
  }

}
