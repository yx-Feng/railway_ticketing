import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

const MEMBER = "MEMBER"

export const useMemberStore = defineStore('member', () => {
  const member = ref(window.SessionStorage.get(MEMBER) || {})

  function setMember(_member) {
    member.value = _member
    window.SessionStorage.set(MEMBER, _member)
  }

  return { member, setMember}
})
