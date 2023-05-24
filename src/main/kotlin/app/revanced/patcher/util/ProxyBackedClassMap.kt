package app.revanced.patcher.util

import app.revanced.patcher.util.proxy.ClassProxy
import org.jf.dexlib2.iface.ClassDef

/**
 * A class that represents a set of classes and proxies.
 *
 * @param classes The classes to be backed by proxies.
 */
class ProxyBackedClassMap(internal val classes: MutableMap<String, ClassDef>) : MutableMap<String, ClassDef> {
    internal val proxies: MutableMap<String, ClassProxy> = mutableMapOf()

    /**
     * Add a [ClassDef].
     */
    fun add(classDef: ClassDef) {
        classes[classDef.type] = classDef
    }

    /**
     * Add a [ClassProxy].
     */
    fun add(classProxy: ClassProxy) {
        proxies[classProxy.immutableClass.type] = classProxy
    }

    /**
     * Replace all classes with their mutated versions.
     */
    internal fun replaceClasses() =
        proxies.entries.removeIf { entry ->
            val proxy = entry.value
            // if the proxy is unused, keep it in the list
            if (!proxy.resolved) return@removeIf false

            // if it has been used, replace the original class with the new class
            classes[proxy.immutableClass.type] = proxy.mutableClass

            // return true to remove it from the proxies list
            return@removeIf true
        }

    override fun isEmpty() = classes.isEmpty()
    override val size get() = classes.size

//    override fun containsAll(elements: Collection<ClassDef>) = classes.values.containsAll(elements)
//    override fun contains(element: ClassDef) = classes.contains(element.type)
//    override fun iterator() = classes.values.iterator()

    override val entries: MutableSet<MutableMap.MutableEntry<String, ClassDef>> = classes.entries
    override val keys: MutableSet<String> = classes.keys
    override val values: MutableCollection<ClassDef> = classes.values
    override fun clear() = classes.clear()
    override fun get(key: String): ClassDef? = classes.get(key)
    override fun containsValue(value: ClassDef): Boolean = classes.containsValue(value)
    override fun containsKey(key: String): Boolean = classes.containsKey(key)
    override fun remove(key: String): ClassDef? = classes.remove(key)
    override fun putAll(from: Map<out String, ClassDef>) = classes.putAll(from)
    override fun put(key: String, value: ClassDef): ClassDef? = classes.put(key, value)
}