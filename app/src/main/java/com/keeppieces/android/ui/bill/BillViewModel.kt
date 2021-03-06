package com.keeppieces.android.ui.bill

import android.util.Log
import androidx.lifecycle.ViewModel
import com.keeppieces.android.logic.data.*
import kotlin.concurrent.thread

class BillViewModel : ViewModel() {
    var bill = Bill(
        System.currentTimeMillis().toString(),
        0.00,
        "现金",
        "自己",
        "三餐",
        "早餐",
        "支出"
    )

    fun addBill(bill: Bill) {
//        AccountRepository().createAccount(Account(bill.account, bill.amount))
        thread {
            AccountRepository().createAccount(Account(bill.account, 0.00))
            val account = AccountRepository().getAAccount(bill.account)
            when (bill.type) {
                "支出" -> {
                    val inAccount = Account(account.name, account.amount-bill.amount)
                    inAccount.accountId = account.accountId
                    updateAccount(inAccount)
                }
                "收入" -> {
                    val inAccount = Account(account.name, account.amount+bill.amount)
                    inAccount.accountId = account.accountId
                    updateAccount(inAccount)
                }
                else -> {
                    val accountSecondary = AccountRepository().getAAccount(bill.secondaryCategory)
                    var inAccount = Account(account.name, account.amount-bill.amount)
                    inAccount.accountId = account.accountId
                    updateAccount(inAccount)
                    inAccount = Account(accountSecondary.name, accountSecondary.amount+bill.amount)
                    inAccount.accountId = accountSecondary.accountId
                    updateAccount(inAccount)
                }
            }
            MemberRepository().createMember(Member(bill.member))
            PrimaryCategoryRepository().createPrimaryCategory(PrimaryCategory(bill.primaryCategory))
            SecondaryCategoryRepository().createSecondaryCategory(
                SecondaryCategory(
                    bill.secondaryCategory,
                    bill.primaryCategory
                )
            )
            TypeRepository().createType(Type(bill.type))
            BillRepository().insertBill(bill)
        }
    }

    private fun updateAccount(account: Account) = AccountRepository().updateAccount(account)

    fun updateBill(bill: Bill) {
        thread {
            Log.d("Bill", bill.toString())
            Log.d("Bill ID", bill.billId.toString())
            val pastBill = BillRepository().getABill(bill.billId)
            Log.d("Bill", pastBill.toString())
            val pastAccount = AccountRepository().getAAccount(pastBill.account)
            val nowAccount = AccountRepository().getAAccount(bill.account)
            when (pastBill.type) {
                "支出" -> {
                    val inAccount = Account(pastBill.account, pastAccount.amount + pastBill.amount)
                    inAccount.accountId = pastAccount.accountId
                    updateAccount(inAccount)
                }
                "收入" -> {
                    val inAccount = Account(pastBill.account, pastAccount.amount - pastBill.amount)
                    inAccount.accountId = pastAccount.accountId
                    updateAccount(inAccount)
                }
                else -> {
                    val accountSecondary = AccountRepository().getAAccount(pastBill.secondaryCategory)
                    var inAccount = Account(pastAccount.name, pastAccount.amount+pastBill.amount)
                    inAccount.accountId = pastAccount.accountId
                    updateAccount(inAccount)
                    inAccount = Account(accountSecondary.name, accountSecondary.amount-pastBill.amount)
                    inAccount.accountId = accountSecondary.accountId
                    updateAccount(inAccount)
                }
            }
            when (bill.type){
                "支出" -> {
                    val inAccount = Account(bill.account, nowAccount.amount - bill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                }
                "收入" -> {
                    val inAccount = Account(bill.account, nowAccount.amount + bill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                }
                else -> {
                    val accountSecondary = AccountRepository().getAAccount(bill.secondaryCategory)
                    var inAccount = Account(nowAccount.name, nowAccount.amount-bill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                    inAccount = Account(accountSecondary.name, accountSecondary.amount+bill.amount)
                    inAccount.accountId = accountSecondary.accountId
                    updateAccount(inAccount)
                }
            }
            BillRepository().updateBill(bill)
        }
    }

    fun deleteBill(bill: Bill) {
        thread {
            val pastBill = BillRepository().getABill(bill.billId)
            val nowAccount = AccountRepository().getAAccount(pastBill.account)
            when (pastBill.type) {
                "支出" -> {
                    val inAccount = Account(pastBill.account, nowAccount.amount + pastBill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                }
                "收入" -> {
                    val inAccount = Account(pastBill.account, nowAccount.amount - pastBill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                }
                else -> {
                    val accountSecondary = AccountRepository().getAAccount(pastBill.secondaryCategory)
                    var inAccount = Account(nowAccount.name, nowAccount.amount+pastBill.amount)
                    inAccount.accountId = nowAccount.accountId
                    updateAccount(inAccount)
                    inAccount = Account(accountSecondary.name, accountSecondary.amount-pastBill.amount)
                    inAccount.accountId = accountSecondary.accountId
                    updateAccount(inAccount)
                }
            }
            BillRepository().deleteBill(bill)
        }
    }

    fun findTypeList() = TypeRepository().getType()

    fun findMemberList() = MemberRepository().getMember()

    fun addMember(member: Member) {
        thread {
            MemberRepository().createMember(member)
        }
    }

    fun findPrimaryList() = PrimaryCategoryRepository().getPrimaryCategory()

    fun findSecondaryList() = SecondaryCategoryRepository().getSecondaryCategory()

    fun addPrimary(primaryCategory: PrimaryCategory) {
        thread {
            PrimaryCategoryRepository().createPrimaryCategory(primaryCategory)
        }
    }

    fun addSecondary(secondaryCategory: SecondaryCategory) {
        thread {
            PrimaryCategoryRepository().createPrimaryCategory(PrimaryCategory(secondaryCategory.primaryName))
            SecondaryCategoryRepository().createSecondaryCategory(secondaryCategory)
        }
    }

    fun findAccountList() = AccountRepository().getAccount()

    fun addAccount(account: Account) {
        thread {
            AccountRepository().createAccount(account)
            PrimaryCategoryRepository().createPrimaryCategory(PrimaryCategory("转账"))
            SecondaryCategoryRepository().createSecondaryCategory(SecondaryCategory(account.name,"转账"))
        }
    }
}