/******************************************************************************* 
 * Copyright (c) 2008 - 2014 Red Hat, Inc. and others. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.core.internal.metamodel.builder;

import java.util.EventObject;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jboss.tools.ws.jaxrs.core.internal.utils.ConstantUtils;
import org.jboss.tools.ws.jaxrs.core.jdt.Flags;
import org.jboss.tools.ws.jaxrs.core.jdt.JdtUtils;

/**
 * Event raised when a Java Element of interest for the metamodel is changed.
 * 
 * @author xcoulon
 * 
 */
public class JavaElementChangedEvent extends EventObject {

	/** generated serial version UID */
	private static final long serialVersionUID = 8821221398378359798L;

	public static final int NO_FLAG = 0;

	/**
	 * The original event type.
	 * 
	 * @see ElementChangedEvent.POST_CHANGE
	 * @see ElementChangedEvent.POST_RECONCILE
	 * 
	 */
	private final int eventType;

	/** The java element that changed. */
	private final IJavaElement element;

	/** The kind of change. */
	private final int deltaKind;

	/** Some flags to describe more precisely the kind of change. */
	private final Flags flags;

	/**
	 * the compilation unit AST retrieved from the change event, or null if the
	 * event was not of the POST_RECONCILE type.
	 */
	private final CompilationUnit compilationUnitAST;

	/**
	 * Full constructor.
	 * 
	 * @param element
	 *            The java element that changed.
	 * @param deltaKind
	 *            The kind of change (ADDED, CHANGED, REMOVED).
	 * @param eventType
	 *            The type of event (POST_CHANGE or POST_RECONCILE).
	 * @param compilationUnitAST
	 *            The associated compilation unit AST (or null if it does not
	 *            apply to the given element)
	 * @param flags
	 *            the detailed kind of change.
	 * @see IJavaElementDelta for element change kind values.
	 */
	public JavaElementChangedEvent(final IJavaElement element, final int deltaKind, final int eventType,
			final CompilationUnit compilationUnitAST, final Flags flags) {
		super(element);
		this.element = element;
		this.deltaKind = deltaKind;
		this.eventType = eventType;
		this.compilationUnitAST = compilationUnitAST;
		this.flags = flags;
	}

	/**
	 * @return the element
	 */
	public IJavaElement getElement() {
		return element;
	}

	/**
	 * @return the deltaKind
	 */
	public int getKind() {
		return deltaKind;
	}

	/**
	 * @return the eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @return the compilationUnitAST
	 */
	public CompilationUnit getCompilationUnitAST() {
		return compilationUnitAST;
	}

	/**
	 * @return the flags
	 */
	public Flags getFlags() {
		return flags;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("JavaElementDelta ").append("[")
				.append(ConstantUtils.getStaticFieldName(ElementChangedEvent.class, eventType)).append("] ")
				.append(ConstantUtils.getStaticFieldName(IJavaElement.class, element.getElementType())).append(" '")
				.append(element.getElementName()).append("' ");
		if (JdtUtils.isWorkingCopy(element)) {
			result.append(" [*working copy*]");
		}
		if (compilationUnitAST != null) {
			result.append("[with AST] ");
		} else {
			result.append("[*without* AST] ");
		}
		result.append(ConstantUtils.getStaticFieldName(IJavaElementDeltaFlag.class, deltaKind).toLowerCase());
		if (flags.hasValue()) {
			int[] f = ConstantUtils.splitConstants(IJavaElementDeltaFlag.class, flags.getValue(), "F_"); //$NON-NLS-1$
			result.append(":{");

			for (int i = 0; i < f.length; i++) {
				result.append(ConstantUtils.getStaticFieldName(IJavaElementDeltaFlag.class, f[i], "F_"));
				if (i < f.length - 1) {
					result.append("+");
				}
			}
			result.append("}");
		} else {
			result.append(":{no flag}");
		}
		result.append("]");
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deltaKind;
		result = prime * result + eventType;
		result = prime * result + ((element == null) ? 0 : element.getElementType());
		result = prime * result + ((element == null) ? 0 : element.getElementName().hashCode());
		result = prime * result + flags.getValue();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final JavaElementChangedEvent other = (JavaElementChangedEvent) obj;
		if (deltaKind != other.deltaKind) {
			return false;
		}
		if (eventType != other.eventType) {
			return false;
		}
		if (this.element == null) {
			return false;
		} else if (other.element == null) {
			return false;
		} else if (!element.getElementName().equals(other.element.getElementName())
						|| element.getElementType() != other.element.getElementType()) {
			return false;
		}
		if (!flags.equals(other.flags)) {
			return false;
		}
		return true;
	}
}
