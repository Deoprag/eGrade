package com.deopraglabs.egrade.util

import com.deopraglabs.egrade.model.User
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

object DatabaseUtil {
    private val sessionFactory: SessionFactory by lazy {
        val registry = StandardServiceRegistryBuilder().configure().build()
        MetadataSources(registry).buildMetadata().buildSessionFactory()
    }

    fun getSession(): Session {
        return sessionFactory.openSession()
    }
}

fun main() {
    val session = DatabaseUtil.getSession()
    session.beginTransaction()

    val users = session.createQuery("FROM User", User::class.java).list()
    for (user in users) {
        println("ID: ${user.id}, Username: ${user.cpf}, Email: ${user.email}")
    }

    session.transaction.commit()
    session.close()
}
