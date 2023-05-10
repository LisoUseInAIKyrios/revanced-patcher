package app.revanced.patcher.util

import app.revanced.patcher.util.proxy.ClassProxy
import org.jf.dexlib2.iface.ClassDef

/**
 * A class that represents a set of classes and proxies.
 *
 * @param classes The classes to be backed by proxies.
 */
class ProxyBackedClassList(internal val classes: MutableMap<String, ClassDef>) : Set<ClassDef> {
    internal val proxies = mutableListOf<ClassProxy>()

    /**
     * Add a [ClassDef].
     */
    fun add(classDef: ClassDef) {
        classes[classDef.type] = classDef
    }

    /**
     * Add a [ClassProxy].
     */
    fun add(classProxy: ClassProxy) = proxies.add(classProxy)

    /**
     * Replace all classes with their mutated versions.
     */
    internal fun replaceClasses() =
        proxies.removeIf { proxy ->
            // if the proxy is unused, keep it in the list
            if (!proxy.resolved) return@removeIf false

            // if it has been used, replace the original class with the new class
            classes[proxy.immutableClass.type] = proxy.immutableClass

            // return true to remove it from the proxies list
            return@removeIf true
        }


    override val size get() = classes.size
    override fun contains(element: ClassDef) = classes.containsValue(element)
    override fun containsAll(elements: Collection<ClassDef>) = classes.values.containsAll(elements)
    override fun isEmpty() = classes.isEmpty()
    override fun iterator(): Iterator<ClassDef> = classes.values.iterator()
}