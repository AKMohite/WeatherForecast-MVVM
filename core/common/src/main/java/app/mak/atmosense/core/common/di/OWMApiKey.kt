package app.mak.atmosense.core.common.di

import javax.inject.Qualifier

@Qualifier
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
annotation class OWMApiKey
