package br.ufpe.cin.if710.rss

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

// Fragmento para renderizar a FragmentScreen com a preferencia do feed
class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferencias)
    }
}